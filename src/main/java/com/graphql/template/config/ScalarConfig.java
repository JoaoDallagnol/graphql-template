package com.graphql.template.config;

import com.graphql.template.scalar.DateTimeCoercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class ScalarConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        GraphQLScalarType dateTimeScalar = GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("Java LocalDateTime as ISO-8601 string")
                .coercing(new DateTimeCoercing())
                .build();

        return wiringBuilder -> wiringBuilder.scalar(dateTimeScalar);
    }
}