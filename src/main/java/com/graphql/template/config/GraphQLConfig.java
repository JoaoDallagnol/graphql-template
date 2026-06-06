package com.graphql.template.config;

import com.graphql.template.cache.CachingPreparsedDocumentProvider;
import org.springframework.boot.graphql.autoconfigure.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures GraphQL engine to use custom caching provider for parsed documents.
 * Hooks CachingPreparsedDocumentProvider into Spring GraphQL execution pipeline.
 */
@Configuration
public class GraphQLConfig {

    /**
     * Customizes GraphQL builder to inject preparsed document provider.
     * This ensures all queries go through our caching layer during parsing phase.
     *
     * @param provider the caching provider instance
     * @return customizer that modifies GraphQL engine configuration
     */
    @Bean
    public GraphQlSourceBuilderCustomizer customizer(
            CachingPreparsedDocumentProvider provider) {
        return builder -> builder.configureGraphQl(graphQLBuilder ->
                graphQLBuilder.preparsedDocumentProvider(provider));
    }
}
