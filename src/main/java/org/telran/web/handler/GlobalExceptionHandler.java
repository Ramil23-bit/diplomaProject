package org.telran.web.handler;

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

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CategoryNotFoundException.class,
                        StorageNotFoundException.class,
                        ProductNotFoundException.class,
                        UserNotFoundException.class,
                        CartItemsNotFoundException.class,
                        CartNotFoundException.class,
                        PaymentNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException validException,
                                                                  HttpHeaders headers, HttpStatusCode statusCode,
                                                                  WebRequest request){
        Map<String, String> errorMap = new HashMap<>();
        List<ObjectError> errorList = validException.getBindingResult().getAllErrors();
        for(ObjectError error: errorList){
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(field, defaultMessage);
        }
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
