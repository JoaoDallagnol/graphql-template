package com.graphql.template.dto;

import java.time.LocalDateTime;

public record AuthorDTO (
        Long id,
        String firstName,
        String lastName,
        LocalDateTime createAt
) {}