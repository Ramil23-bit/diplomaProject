package org.telran.web.exception;

public class CartItemsNotFoundException extends RuntimeException{

    public CartItemsNotFoundException(String message) {
        super(message);
    }
}
