package com.graphql.template.resolver;

import com.graphql.template.dto.BookDTO;
import com.graphql.template.dto.BookInput;
import com.graphql.template.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookMutationResolver {

    private final BookService bookService;

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
