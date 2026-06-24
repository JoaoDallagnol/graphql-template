package com.graphql.template.dto;

import java.util.List;

public record AuthorConnection (
        List<AuthorEdge> edges,
        PageInfo pageInfo,
        Integer totalCount
)
{}
