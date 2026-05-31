package com.graphql.template.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.execution.DefaultExecutionGraphQlService;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.test.tester.GraphQlTester;

/**
 * Configuração de teste para GraphQL.
 * Cria o GraphQlTester a partir do GraphQlSource.
 */
@TestConfiguration
public class TestGraphQlConfig {

    @Bean
    public GraphQlTester graphQlTester(GraphQlSource graphQlSource) {
        // Cria ExecutionGraphQlService a partir do GraphQlSource
        ExecutionGraphQlService service = new DefaultExecutionGraphQlService(graphQlSource);
        // Cria GraphQlTester a partir do service
        return org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester.builder(service).build();
    }
}
