package com.wilderBackend.utils;

import com.wilderBackend.exception.ResourceNotFoundException;
import com.wilderBackend.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

public class ExceptionUtils {

    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static ResponseEntity<Response> handleException(Exception exception) {
        log.error("Exception occurred: ", exception);
        HttpStatus status =
                exception instanceof ResourceNotFoundException ? HttpStatus.NOT_FOUND :
                        exception instanceof IllegalArgumentException ? HttpStatus.BAD_REQUEST :
                                exception instanceof AccessDeniedException ? HttpStatus.FORBIDDEN :
                                        exception instanceof BadCredentialsException ? HttpStatus.UNAUTHORIZED :
                                                HttpStatus.INTERNAL_SERVER_ERROR;

        String message = exception.getMessage() != null ? exception.getMessage() : "Unexpected error, Please try again later.";

        Response resp = Response.builder()
                .status(status.value())
                .errors(List.of(message))
                .build();

        return ResponseEntity.status(status).body(resp);
    }
}
