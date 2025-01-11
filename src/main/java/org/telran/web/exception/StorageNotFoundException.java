package org.telran.web.exception;

public class StorageNotFoundException extends RuntimeException{
    public StorageNotFoundException(String message) {
        super(message);
    }
}
