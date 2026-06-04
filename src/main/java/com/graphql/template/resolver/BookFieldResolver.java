package com.graphql.template.resolver;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookFieldResolver {

    private final AuthorService authorService;


    // Field resolver: Resolves the 'author' field for Book type
    @SchemaMapping(typeName = "Book", field = "author")
    public AuthorDTO author(BookDTO book) {
        return authorService.getById(book.authorId());
    }
}
