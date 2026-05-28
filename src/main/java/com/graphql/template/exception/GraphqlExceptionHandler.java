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

    @Override
    protected GraphQLError resolveToSingleError(
            Throwable ex,
            DataFetchingEnvironment env
    ) {
        return switch (ex) {
            case NotFoundException e -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .extensions(Map.of("code", e.getErrorCode().getCode()))
                    .build();
            case IllegalArgumentException illegalArgumentException -> GraphqlErrorBuilder.newError(env)
                    .message(ErrorCode.INVALID_ID.getMessage())
                    .extensions(Map.of("code", ErrorCode.INVALID_ID.getCode()))
                    .build();
            case DeletionNotAllowedException e -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .extensions(Map.of("code", e.getErrorCode().getCode()))
                    .build();
            default -> null;
        };
    }
}