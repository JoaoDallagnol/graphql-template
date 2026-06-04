package com.graphql.template.resolver;

import com.graphql.template.dto.BookDTO;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookQueryResolver {

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
}