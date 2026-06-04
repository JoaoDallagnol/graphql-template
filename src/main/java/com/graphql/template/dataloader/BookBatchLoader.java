package com.graphql.template.dataloader;

import com.graphql.template.dto.AuthorDTO;
import com.graphql.template.dto.BookDTO;
import com.graphql.template.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookBatchLoader {
    private final AuthorService authorService;

    @BatchMapping(typeName = "Book", field = "author")
    public Mono<Map<BookDTO, AuthorDTO>> author(List<BookDTO> books) {

        //extract author ids
        List<Long> authorIds = books.stream()
                .map(BookDTO::authorId)
                .distinct()
                .toList();

        //Does ONE query for all the authors (no 1+N problem)
        List<AuthorDTO> authors = authorService.getByIds(authorIds);

        //Create a map authorId -> AuthorDTO
        Map<Long, AuthorDTO> authorsById = authors.stream()
                .collect(Collectors.toMap(AuthorDTO::id, author -> author));

        // Create a map BookDTO -> AuthorDTO (maps each book to its author)
        // The KEY must be the BookDTO object itself, not just an ID
        Map<BookDTO, AuthorDTO> result = books.stream()
                .collect(Collectors.toMap(
                        book -> book,  // ← KEY: BookDTO object
                        book -> authorsById.get(book.authorId())  // ← VALUE: Author
                ));

        // Return new Mono
        return Mono.just(result);
    }
}
