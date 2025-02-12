package org.telran.web.exception;

public class FavoritesNotFoundException extends RuntimeException {
    public FavoritesNotFoundException(String message) {
        super(message);
    }
}
