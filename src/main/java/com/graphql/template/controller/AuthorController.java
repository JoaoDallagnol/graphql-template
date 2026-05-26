package com.graphql.template.controller;

import com.graphql.template.data.Author;
import com.graphql.template.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * This method resolves the 'authors' query defined in the GraphQL schema (author.graphqls).
     * The @QueryMapping annotation tells Spring GraphQL to map the 'authors' query
     * to this method.
     * @return A list of all authors.
     */
    @QueryMapping
    public List<Author> authors() {
        return authorService.getAllAuthors();
    }
}