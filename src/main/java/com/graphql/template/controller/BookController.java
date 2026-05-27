package com.graphql.template.controller;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    /**
     * This method resolves the 'books' query defined in the GraphQL schema (book.graphqls).
     * The @QueryMapping annotation tells Spring GraphQL to map the 'books' query
     * to this method.
     * @return A list of all books.
     */
    @QueryMapping
    public List<BookDTO> books() {
        return bookService.books();
    }

    /**
     * This method resolves the 'bookById' query defined in the GraphQL schema (book.graphqls).
     * It fetches a single book by its ID.
     * The @Argument annotation binds the 'id' argument from the GraphQL query to this method parameter.
     * @param id The ID of the book to retrieve.
     * @return The Book object matching the given ID, or null if not found.
     */
    @QueryMapping
    public BookDTO bookById(@Argument Long id) {
        return bookService.bookById(id);
    }

    /**
     * This method resolves the 'booksByAuthor' query defined in the GraphQL schema (book.graphqls).
     * It fetches a list of books written by a specific author ID.
     * The @Argument annotation binds the 'id' argument from the GraphQL query to this method parameter.
     * @param id The ID of the author whose books to retrieve.
     * @return A list of Book objects written by the specified author.
     */
    @QueryMapping
    public List<BookDTO> booksByAuthor(@Argument Long id) {
        return bookService.booksByAuthor(id);
    }

    @QueryMapping
    public List<BookDTO> booksWithFilter(
            @Argument Long id,
            @Argument String authorId,
            @Argument String name) {
        return bookService.books(id, authorId, name);
    }

    /**
     * This method resolves the 'author' field within a 'Book' type in the GraphQL schema.
     * The @SchemaMapping annotation indicates that this method is a field resolver.
     * When a GraphQL query requests the 'author' field of a 'Book', this method is called.
     * It receives the parent 'Book' object and uses its authorId to fetch the corresponding Author.
     * @param book The parent Book object for which the author is being resolved.
     * @return The Author object associated with the book.
     */
    @SchemaMapping
    public AuthorDTO author(BookDTO book) {
        return authorService.getById(book.authorId());
    }
}