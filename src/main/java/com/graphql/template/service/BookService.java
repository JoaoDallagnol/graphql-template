package com.graphql.template.service;

import com.graphql.template.data.Book;
import com.graphql.template.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> books() {return bookRepository.getAllBooks();}

    public Book bookById(String id) {
        return bookRepository.getById(id);
    }

    public List<Book> booksByAuthor(String id) {return bookRepository.getBooksByAuthor(id);}
}
