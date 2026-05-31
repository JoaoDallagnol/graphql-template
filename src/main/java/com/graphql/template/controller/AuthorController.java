package com.graphql.template.controller;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.AuthorInput;
import com.graphql.template.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // Query: Fetches all authors
    @QueryMapping
    public List<AuthorDTO> authors() {
        return authorService.getAllAuthors();
    }

    // Mutation: Creates a new author with the provided input data
    @MutationMapping
    public AuthorDTO createAuthor(@Argument AuthorInput author) {
        return authorService.createAuthor(author);
    }

    // Mutation: Updates an existing author by ID with new data
    @MutationMapping
    public AuthorDTO updateAuthor(@Argument Long id, @Argument AuthorInput author) {
        return authorService.updateAuthor(id, author);
    }

    // Mutation: Deletes an author by ID and returns the deleted author's ID
    @MutationMapping
    public Long deleteAuthor(@Argument Long id) {
        return authorService.deleteAuthor(id);
    }
}