package net.greeta.stock.order.domain.exception;

import java.util.UUID;

public class OrderCompensationStatusNotFoundException extends RuntimeException {

    public OrderCompensationStatusNotFoundException(UUID orderId) {
        super("Order compensation status for order id %s not found".formatted(orderId));
    }
}

