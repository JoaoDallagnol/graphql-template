package com.graphql.template.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BOOK_NOT_FOUND("BOOK_NOT_FOUND","Book not found"),
    AUTHOR_NOT_FOUND("AUTHOR_NOT_FOUND","Author not found"),
    INVALID_ID("INVALID_ID","Invalid ID");

    private final String code;
    private final String message;
}