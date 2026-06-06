package com.graphql.template.config;

import graphql.analysis.FieldComplexityCalculator;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class GraphQLComplexityConfig {

    // Rejects queries nested deeper than 7 levels before execution
    @Bean
    public Instrumentation depthInstrumentation() {
        return new MaxQueryDepthInstrumentation(7);
    }

    @Bean
    public Instrumentation complexityInstrumentation() {

        FieldComplexityCalculator calculator = (env, childComplexity) -> {
            String fieldName = env.getField().getName();

            // Extract pagination argument to use as cost multiplier
            Map<String, Object> arguments = env.getArguments();
            Object firstArg = arguments.get("first");

            // Cap multiplier at 100 to prevent abuse via large pagination values
            int multiplier = (firstArg instanceof Integer limit)
                    ? Math.min(limit, 100)
                    : 1;

            // Assign cost per field type — list fields cost more due to DB weight
            return switch (fieldName) {
                case "books", "authors" -> (10 + childComplexity) * multiplier;
                case "author" -> 5 + childComplexity; // single relation, cheaper
                default -> 1; // scalar fields cost 1
            };
        };

        // Rejects any query whose total calculated cost exceeds 100
        return new MaxQueryComplexityInstrumentation(100, calculator);
    }
}