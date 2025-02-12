package org.telran.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.telran.web.exception.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for managing application-wide exceptions.
 * Handles specific exception cases and provides meaningful responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles entity not found exceptions.
     *
     * @param exception The thrown exception.
     * @return ResponseEntity with the exception message and NOT_FOUND status.
     */
    @ExceptionHandler({CategoryNotFoundException.class,
            StorageNotFoundException.class,
            ProductNotFoundException.class,
            UserNotFoundException.class,
            CartItemsNotFoundException.class,
            CartNotFoundException.class,
            PaymentNotFoundException.class,
            OrderNotFoundException.class,
            FavoritesNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception exception) {
        logger.error("Not Found Exception: {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation exceptions for request bodies.
     *
     * @param validException The exception containing validation errors.
     * @param headers HTTP headers.
     * @param statusCode HTTP status code.
     * @param request Web request details.
     * @return ResponseEntity containing validation error details.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException validException,
                                                                  HttpHeaders headers, HttpStatusCode statusCode,
                                                                  WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        List<ObjectError> errorList = validException.getBindingResult().getAllErrors();

        for (ObjectError error : errorList) {
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(field, defaultMessage);
        }

        logger.warn("Validation failed: {}", errorMap);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general bad request exceptions.
     *
     * @param ex The exception containing bad request details.
     * @return ResponseEntity with the exception message and BAD_REQUEST status.
     */
    @ExceptionHandler({UserAlreadyExistsException.class, BadArgumentsException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleUserException(RuntimeException ex) {
        logger.warn("Bad Request Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
