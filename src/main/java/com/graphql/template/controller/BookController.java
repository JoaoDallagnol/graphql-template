package com.graphql.template.controller;

import com.graphql.template.data.Author;
import com.graphql.template.data.Book;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @QueryMapping
    public List<Book> books() {return Book.getAllBooks();}

    @QueryMapping
    public Book bookById(@Argument String id) {
        return Book.getById(id);
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument String id) {return Book.getBooksByAuthor(id);}

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.authorId());
    }
}
