package com.graphql.template.data;

import org.springframework.stereotype.Component;

@Component
public record Book(
        String id,
        String name,
        int pageCount,
        String authorId
) {}
