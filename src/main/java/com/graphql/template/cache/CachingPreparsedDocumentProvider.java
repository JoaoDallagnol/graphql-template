package com.graphql.template.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import graphql.ExecutionInput;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caches parsed GraphQL documents to avoid reparsing the same queries.
 * Implements PreparsedDocumentProvider to hook into GraphQL execution pipeline.
 */
@Component
public class CachingPreparsedDocumentProvider implements PreparsedDocumentProvider {

    /**
     * Stores parsed GraphQL documents with TTL eviction.
     * Key: query string, Value: parsed document entry
     */
    private final Cache<String, PreparsedDocumentEntry> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    /**
     * Retrieves or parses GraphQL document and returns it from cache.
     * If query is already cached, returns cached entry (fast path).
     * If not in cache, calls computeDocument to parse it and stores result.
     *
     * @param executionInput contains the incoming GraphQL query text
     * @param computeDocument function to parse query if not cached
     * @return future with parsed document entry
     */
    @Override
    public CompletableFuture<PreparsedDocumentEntry> getDocumentAsync(
            ExecutionInput executionInput,
            Function<ExecutionInput, PreparsedDocumentEntry> computeDocument) {

        PreparsedDocumentEntry entry = cache.get(
                executionInput.getQuery(),
                query -> computeDocument.apply(executionInput)
        );

        return CompletableFuture.completedFuture(entry);
    }
}
