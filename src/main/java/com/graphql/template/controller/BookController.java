package com.graphql.template.controller;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.dto.BookInput;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    // Query: Fetches all books
    @QueryMapping
    public List<BookDTO> books() {
        return bookService.books();
    }

    // Query: Fetches a single book by ID
    @QueryMapping
    public BookDTO bookById(@Argument Long id) {
        return bookService.bookById(id);
    }

    // Query: Fetches all books written by a specific author
    @QueryMapping
    public List<BookDTO> booksByAuthor(@Argument Long id) {
        return bookService.booksByAuthor(id);
    }

    // Query: Fetches books with optional filters (id, authorId, name)
    @QueryMapping
    public List<BookDTO> booksWithFilter(
            @Argument Long id,
            @Argument String authorId,
            @Argument String name) {
        return bookService.books(id, authorId, name);
    }

    // Field resolver: Resolves the 'author' field for Book type
    @SchemaMapping(typeName = "Book", field = "author")
    public AuthorDTO author(BookDTO book) {
        return authorService.getById(book.authorId());
    }

    // Mutation: Creates a new book with the provided input data
    @MutationMapping
    public BookDTO createBook(@Argument BookInput book) {
        return bookService.createBook(book);
    }

    // Mutation: Updates an existing book by ID with new data
    @MutationMapping
    public BookDTO updateBook(@Argument Long id, @Argument BookInput book) {
        return bookService.updateBook(id, book);
    }

    // Mutation: Deletes a book by ID and returns the deleted book's ID
    @MutationMapping
    public Long deleteBook(@Argument Long id) {
        return bookService.deleteBook(id);
    }
}