package net.greeta.stock.order.domain.exception;

import java.util.UUID;

public class OrderWorkflowStatusNotFoundException extends RuntimeException {

    public OrderWorkflowStatusNotFoundException(UUID orderId) {
        super("Order workflow status for order id %s not found".formatted(orderId));
    }
}
