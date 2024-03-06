package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderRequest;

import java.util.UUID;

public interface CreateOrderUseCasePort {

    UUID placeOrder(OrderRequest orderRequest);
}
