package com.graphql.template.exception;

import com.graphql.template.constants.ErrorCode;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    /**
     * Resolves exceptions to GraphQL errors with appropriate error codes and messages.
     * Handles examples: NotFoundException, DeletionNotAllowedException, IllegalArgumentException
     */
    @Override
    protected GraphQLError resolveToSingleError(
            Throwable ex,
            DataFetchingEnvironment env
    ) {
        return switch (ex) {
            // Handle resource not found errors
            case NotFoundException e -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .extensions(Map.of("code", e.getErrorCode().getCode()))
                    .build();
            // Handle invalid ID format errors
            case IllegalArgumentException illegalArgumentException -> GraphqlErrorBuilder.newError(env)
                    .message(ErrorCode.INVALID_ID.getMessage())
                    .extensions(Map.of("code", ErrorCode.INVALID_ID.getCode()))
                    .build();
            // Handle deletion constraint violations
            case DeletionNotAllowedException e -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .extensions(Map.of("code", e.getErrorCode().getCode()))
                    .build();
            default -> null;
        };
    }
}