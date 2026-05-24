package com.graphql.template.controller;

import com.graphql.template.data.Author;
import com.graphql.template.data.Book;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @QueryMapping
    public List<Book> books() {return bookService.books();}

    @QueryMapping
    public Book bookById(@Argument String id) {
        return bookService.bookById(id);
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument String id) {return bookService.booksByAuthor(id);}

    @SchemaMapping
    public Author author(Book book) {
        return authorService.getById(book.authorId());
    }
}
