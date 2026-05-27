package com.graphql.template.dto;

public record BookDTO(
        Long id,
        String name,
        int pageCount,
        Long authorId
) {}
