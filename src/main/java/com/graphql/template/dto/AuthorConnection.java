package com.graphql.template.dto;

import java.util.List;

// Edges: list of paginated items with cursors
public record AuthorConnection (
        List<AuthorEdge> edges,
        PageInfo pageInfo,
        Integer totalCount
)
{}
