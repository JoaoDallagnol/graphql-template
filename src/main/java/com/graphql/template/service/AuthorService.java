package com.graphql.template.service;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.mapper.AuthorMapper;
import com.graphql.template.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toDto).toList();
    }

    public AuthorDTO getById(String id) {
        return authorRepository.findById(UUID.fromString(id)).map(authorMapper::toDto).orElse(null);
    }
}