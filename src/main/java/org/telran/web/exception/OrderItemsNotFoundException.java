package org.telran.web.exception;

public class OrderItemsNotFoundException extends RuntimeException {

    public OrderItemsNotFoundException(String message) {
        super(message);
    }
}
