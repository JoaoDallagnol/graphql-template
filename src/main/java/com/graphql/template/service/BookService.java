package com.graphql.template.service;

import com.graphql.template.dto.BookDTO;
import com.graphql.template.mapper.BookMapper;
import com.graphql.template.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> books() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    public List<BookDTO> books(String id, String authorId, String name) {
        return bookRepository.findBooksWithFilters(id, authorId, name)
                .stream().map(bookMapper::toDto).toList();
    }

    public BookDTO bookById(String id) {
        return bookRepository.findById(UUID.fromString(id)).map(bookMapper::toDto).orElse(null);
    }

    public List<BookDTO> booksByAuthor(String id) {
        return bookRepository.findBooksByAuthorId(id).stream().map(bookMapper::toDto).toList();
    }
}