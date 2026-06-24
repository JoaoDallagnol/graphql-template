package com.graphql.template.dto;

public record PageInfo (
        Boolean hasNextPage,
        Boolean hasPreviousPage,
        String startCursor,
        String endCursor
)
{}
