package com.graphql.template.service;

import com.graphql.template.constants.ErrorCode;
import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.exception.NotFoundException;
import com.graphql.template.mapper.AuthorMapper;
import com.graphql.template.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toDto).toList();
    }

    public AuthorDTO getById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto).orElseThrow(
                () -> new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));
    }
}