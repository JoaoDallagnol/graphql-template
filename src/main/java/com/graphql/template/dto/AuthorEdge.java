package com.graphql.template.dto;

/**
 * Wraps a paginated item with its cursor for use in connections.
 * Node: the actual data (AuthorDTO)
 * Cursor: opaque identifier for cursor-based pagination
 */
public record AuthorEdge (
        AuthorDTO node,
        String cursor
)
{}
