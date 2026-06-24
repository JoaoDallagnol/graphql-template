package com.graphql.template.resolver;

import com.graphql.template.dto.AuthorConnection;
import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.service.AuthorService;
import com.graphql.template.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorQueryResolver {

    private final AuthorService authorService;
    private final PaginationService paginationService;

    // Query: Fetches all authors
    @QueryMapping
    public List<AuthorDTO> authors() {
        return authorService.getAllAuthors();
    }

    @QueryMapping
    public AuthorConnection authorsWithPagination(
            @Argument Integer first,
            @Argument String after,
            @Argument Integer last,
            @Argument String before) {

        return paginationService.getAuthorsWithPagination(first, after, last, before);
    }
}