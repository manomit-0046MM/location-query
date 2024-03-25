package com.bsolz.locationquery.exception;


import lombok.Getter;

@Getter
public class InputValidationException extends RuntimeException {

    private static final String MSG = "Invalid query params";
    private static final int ERROR_CODE = 403;

    private final int input;

    public InputValidationException(int input) {
        super(MSG);
        this.input = input;
    }

    public int getInput() {
        return input;
    }

    public int getErrorCode() {
        return ERROR_CODE;
    }
}
