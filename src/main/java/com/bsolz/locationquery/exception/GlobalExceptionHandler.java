package com.bsolz.locationquery.exception;

import com.bsolz.locationquery.exception.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<ApiResponse> handleException(InputValidationException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse(ex.getErrorCode(), ex.getMessage(), new SimpleDateFormat("MMM dd,yyyy HH:mm").format(new Date(System.currentTimeMillis()))));
    }
}
