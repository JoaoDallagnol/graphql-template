package com.graphql.template.exception;

import com.graphql.template.constants.ErrorCode;
import lombok.Getter;

@Getter
public class DeletionNotAllowedException extends RuntimeException {
    private final ErrorCode errorCode;

    public DeletionNotAllowedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
