package com.graphql.template.dto;

public record BookInput(
        String name,
        int pageCount,
        Long authorId
) {
}
