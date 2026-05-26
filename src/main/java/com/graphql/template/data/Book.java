package com.graphql.template.data;

public record Book(
        String id,
        String name,
        int pageCount,
        String authorId
) {}
