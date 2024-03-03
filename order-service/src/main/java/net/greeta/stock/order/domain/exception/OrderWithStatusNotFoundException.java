package net.greeta.stock.order.domain.exception;

import net.greeta.stock.common.domain.dto.order.OrderStatus;

import java.util.UUID;

public class OrderWithStatusNotFoundException extends RuntimeException {

    public OrderWithStatusNotFoundException(UUID orderId, OrderStatus orderStatus) {
        super("Order with id %s and status %s not found".formatted(orderId, orderStatus));
    }
}
