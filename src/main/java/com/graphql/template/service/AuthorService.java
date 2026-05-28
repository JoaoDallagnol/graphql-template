package com.graphql.template.service;

import com.graphql.template.constants.ErrorCode;
import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.AuthorInput;
import com.graphql.template.entity.AuthorEntity;
import com.graphql.template.exception.DeletionNotAllowedException;
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

    public AuthorDTO createAuthor(AuthorInput author) {
        AuthorEntity entity = authorMapper.toEntity(author);
        entity = authorRepository.save(entity);
        return authorMapper.toDto(entity);
    }

    public AuthorDTO updateAuthor(Long id, AuthorInput authorInput) {
        AuthorEntity entity = authorRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));
        entity.setName(authorInput.firstName());
        entity.setLastName(authorInput.lastName());
        authorRepository.save(entity);
        return authorMapper.toDto(entity);
    }

    public Long deleteAuthor(Long id) {
        AuthorEntity entity = authorRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));
        validateAuthorBooks(entity);
        authorRepository.delete(entity);
        return id;
    }

    private void validateAuthorBooks(AuthorEntity author) {
        if (!author.getBooks().isEmpty()) {
            throw new DeletionNotAllowedException(ErrorCode.DELETION_NOT_ALLOWED_FOR_AUTHOR);
        }
    }
}