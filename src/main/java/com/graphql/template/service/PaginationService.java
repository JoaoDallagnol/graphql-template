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

/**
 * Handles cursor-based pagination logic for Authors using Relay specification.
 * Supports bidirectional pagination: forward (first/after) and backward (last/before).
 */
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

        // Fetch in appropriate direction
        if (isForward) {
            entities = fetchForward(first, after);
        } else {
            entities = fetchBackward(last, before);
        }

        // Determine if we fetched more than requested (lookahead)
        int pageSize = isForward ? first : last;
        hasExtra = entities.size() > pageSize;

        // Trim the extra lookahead item (not returned to client)
        if (hasExtra) {
            entities = entities.subList(0, pageSize);
        }

        // Backward results come DESC from DB — reverse to ASC for consistency
        if (!isForward) {
            entities = new java.util.ArrayList<>(entities);
            Collections.reverse(entities);
        }

        // Map entities to edges (attach cursor to each item)
        List<AuthorEdge> edges = entities.stream()
                .map(entity -> new AuthorEdge(
                        authorMapper.toDto(entity),
                        CursorUtil.encode(entity.getId())
                ))
                .toList();

        // Build pagination metadata for client navigation
        PageInfo pageInfo = buildPageInfo(edges, isForward, hasExtra, after, before);

        // Return complete connection with edges, pageInfo, and total count
        return new AuthorConnection(edges, pageInfo, (int) authorRepository.count());
    }

    //Fetches items in forward direction (ascending by ID).
    //If cursor provided, starts after that ID. Otherwise, starts from beginning.
    private List<AuthorEntity> fetchForward(Integer first, String after) {
        Pageable pageable = PageRequest.of(0, first + 1);  // +1 for lookahead

        if (after != null) {
            Long cursorId = CursorUtil.decode(after);
            return authorRepository.findByIdGreaterThanOrderByIdAsc(cursorId, pageable);
        }
        return authorRepository.findByOrderByIdAsc(pageable);
    }

    //Fetches items in backward direction (descending by ID).
    //If cursor provided, starts before that ID. Otherwise, starts from end.
    private List<AuthorEntity> fetchBackward(Integer last, String before) {
        Pageable pageable = PageRequest.of(0, last + 1);  // +1 for lookahead

        if (before != null) {
            Long cursorId = CursorUtil.decode(before);
            return authorRepository.findByIdLessThanOrderByIdDesc(cursorId, pageable);
        }
        return authorRepository.findByOrderByIdDesc(pageable);
    }

    private PageInfo buildPageInfo(
            List<AuthorEdge> edges, boolean isForward, boolean hasExtra,
            String after, String before) {

        // Empty result set
        if (edges.isEmpty()) {
            return new PageInfo(false, false, null, null);
        }

        // Get boundary cursors for client to use in next request
        String startCursor = edges.getFirst().cursor();
        String endCursor = edges.getLast().cursor();

        // Determine page navigation flags
        boolean hasNextPage = isForward ? hasExtra : (before != null);
        boolean hasPreviousPage = isForward ? (after != null) : hasExtra;

        return new PageInfo(hasNextPage, hasPreviousPage, startCursor, endCursor);
    }
}
