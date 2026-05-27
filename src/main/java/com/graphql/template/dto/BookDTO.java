package com.graphql.template.dto;

public record BookDTO(
        String id,
        String name,
        int pageCount,
        String authorId
) {}
