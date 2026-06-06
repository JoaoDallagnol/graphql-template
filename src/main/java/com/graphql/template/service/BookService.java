package com.graphql.template.service;

import com.graphql.template.constants.ErrorCode;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.dto.BookInput;
import com.graphql.template.entity.AuthorEntity;
import com.graphql.template.entity.BookEntity;
import com.graphql.template.exception.NotFoundException;
import com.graphql.template.mapper.BookMapper;
import com.graphql.template.repository.AuthorRepository;
import com.graphql.template.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> books() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<BookDTO> books(Long id, String authorId, String name) {
        return bookRepository.findBooksWithFilters(id, authorId, name)
                .stream().map(bookMapper::toDto).toList();
    }

    @Cacheable(value = "book", key = "#id")
    public BookDTO bookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(
                () -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
    }

    public List<BookDTO> booksByAuthor(Long id) {
        return bookRepository.findBooksByAuthorId(id).stream().map(bookMapper::toDto).toList();
    }

    @CacheEvict(value = "book", key = "#result.id")
    public BookDTO createBook(BookInput book) {
        AuthorEntity authorEntity = authorRepository.findById(book.authorId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        BookEntity bookEntity = bookMapper.toEntity(book);
        bookEntity.setAuthor(authorEntity);
        bookEntity = bookRepository.save(bookEntity);
        return bookMapper.toDto(bookEntity);
    }

    @CacheEvict(value = "book", key = "#id")
    public BookDTO updateBook(Long id, BookInput book) {
        BookEntity bookEntity = bookRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
        AuthorEntity authorEntity = authorRepository.findById(book.authorId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.AUTHOR_NOT_FOUND));

        bookEntity.setName(book.name());
        bookEntity.setPageCount(book.pageCount());
        bookEntity.setAuthor(authorEntity);
        bookEntity = bookRepository.save(bookEntity);
        return bookMapper.toDto(bookEntity);
    }


    @CacheEvict(value = "book", key = "#id")
    public Long deleteBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).
                orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

        bookRepository.delete(bookEntity);
        return id;
    }
}