package com.graphql.template.service;

import com.graphql.template.constants.ErrorCode;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.exception.NotFoundException;
import com.graphql.template.mapper.BookMapper;
import com.graphql.template.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> books() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    public List<BookDTO> books(Long id, String authorId, String name) {
        return bookRepository.findBooksWithFilters(id, authorId, name)
                .stream().map(bookMapper::toDto).toList();
    }

    public BookDTO bookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(
                () -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
    }

    public List<BookDTO> booksByAuthor(Long id) {
        return bookRepository.findBooksByAuthorId(id).stream().map(bookMapper::toDto).toList();
    }
}