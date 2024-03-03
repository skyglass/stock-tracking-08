package net.greeta.stock.order.domain.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("Order with id %s not found".formatted(orderId));
    }
}
