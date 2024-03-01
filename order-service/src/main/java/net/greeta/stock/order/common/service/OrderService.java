package net.greeta.stock.order.common.service;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {

    Mono<Order> placeOrder(OrderRequest request);

    Mono<OrderDetails> getOrderDetails(UUID orderId);

}
