package com.graphql.template.controller;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /**
     * This method resolves the 'authors' query defined in the GraphQL schema (author.graphqls).
     * The @QueryMapping annotation tells Spring GraphQL to map the 'authors' query
     * to this method.
     * @return A list of all authors.
     */
    @QueryMapping
    public List<AuthorDTO> authors() {
        return authorService.getAllAuthors();
    }
}