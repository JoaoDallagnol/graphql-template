package com.graphql.template.resolver;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorQueryResolver {

    private final AuthorService authorService;

    // Query: Fetches all authors
    @QueryMapping
    public List<AuthorDTO> authors() {
        return authorService.getAllAuthors();
    }
}