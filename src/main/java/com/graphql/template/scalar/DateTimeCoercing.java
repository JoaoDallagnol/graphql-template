package com.graphql.template.scalar;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeCoercing implements Coercing<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Java object -> value sent to client (response)
    @Override
    public String serialize(@NonNull Object dataFetcherResult, @NonNull GraphQLContext context, @NonNull Locale locale)
            throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime localDateTime) {
            return localDateTime.format(FORMATTER);
        }
        throw new CoercingSerializeException("Expected a LocalDateTime object.");
    }

    // Client variable value -> Java object (input)
    @Override
    public LocalDateTime parseValue(@NonNull Object input, @NonNull GraphQLContext context, @NonNull Locale locale)
            throws CoercingParseValueException {
        try {
            return LocalDateTime.parse(input.toString(), FORMATTER);
        } catch (Exception e) {
            throw new CoercingParseValueException("Invalid DateTime value: " + input, e);
        }
    }

    // Literal written in the query string -> Java object
    @Override
    public LocalDateTime parseLiteral(@NonNull Value<?> input, @NonNull CoercedVariables variables,
                                      @NonNull GraphQLContext context, @NonNull Locale locale)
            throws CoercingParseLiteralException {
        if (input instanceof StringValue stringValue) {
            try {
                assert stringValue.getValue() != null;
                return LocalDateTime.parse(stringValue.getValue(), FORMATTER);
            } catch (Exception e) {
                throw new CoercingParseLiteralException("Invalid DateTime literal: " + stringValue.getValue(), e);
            }
        }
        throw new CoercingParseLiteralException("Expected a String literal for DateTime.");
    }
}