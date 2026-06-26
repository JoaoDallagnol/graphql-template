package com.graphql.template.dto;

import java.time.LocalDateTime;

public record BookDTO(
        Long id,
        String name,
        int pageCount,
        Long authorId,
        LocalDateTime createAt
) {}
