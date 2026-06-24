package com.graphql.template.dto;

public record AuthorEdge (
        AuthorDTO node,
        String cursor
)
{}
