package com.graphql.template.service;

import com.graphql.template.Utils.CursorUtil;
import com.graphql.template.dto.AuthorConnection;
import com.graphql.template.dto.AuthorEdge;
import com.graphql.template.dto.PageInfo;
import com.graphql.template.entity.AuthorEntity;
import com.graphql.template.mapper.AuthorMapper;
import com.graphql.template.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaginationService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorConnection getAuthorsWithPagination(
            Integer first, String after, Integer last, String before) {

        boolean isForward = first != null;

        List<AuthorEntity> entities;
        boolean hasExtra;

        if (isForward) {
            entities = fetchForward(first, after);
        } else {
            entities = fetchBackward(last, before);
        }

        int pageSize = isForward ? first : last;
        hasExtra = entities.size() > pageSize;

        // Trim the extra lookahead item
        if (hasExtra) {
            entities = entities.subList(0, pageSize);
        }

        // Backward results come DESC — reverse to ASC for the client
        if (!isForward) {
            entities = new java.util.ArrayList<>(entities);
            Collections.reverse(entities);
        }

        List<AuthorEdge> edges = entities.stream()
                .map(entity -> new AuthorEdge(
                        authorMapper.toDto(entity),
                        CursorUtil.encode(entity.getId())
                ))
                .toList();

        PageInfo pageInfo = buildPageInfo(edges, isForward, hasExtra, after, before);

        return new AuthorConnection(edges, pageInfo, (int) authorRepository.count());
    }

    private List<AuthorEntity> fetchForward(Integer first, String after) {
        Pageable pageable = PageRequest.of(0, first + 1);

        if (after != null) {
            Long cursorId = CursorUtil.decode(after);
            return authorRepository.findByIdGreaterThanOrderByIdAsc(cursorId, pageable);
        }
        return authorRepository.findByOrderByIdAsc(pageable);
    }

    private List<AuthorEntity> fetchBackward(Integer last, String before) {
        Pageable pageable = PageRequest.of(0, last + 1);

        if (before != null) {
            Long cursorId = CursorUtil.decode(before);
            return authorRepository.findByIdLessThanOrderByIdDesc(cursorId, pageable);
        }
        return authorRepository.findByOrderByIdDesc(pageable);
    }

    private PageInfo buildPageInfo(
            List<AuthorEdge> edges, boolean isForward, boolean hasExtra,
            String after, String before) {

        if (edges.isEmpty()) {
            return new PageInfo(false, false, null, null);
        }

        String startCursor = edges.getFirst().cursor();
        String endCursor = edges.getLast().cursor();

        boolean hasNextPage = isForward ? hasExtra : (before != null);
        boolean hasPreviousPage = isForward ? (after != null) : hasExtra;

        return new PageInfo(hasNextPage, hasPreviousPage, startCursor, endCursor);
    }
}
