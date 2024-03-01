package net.greeta.stock.order.common.service;

import net.greeta.stock.common.domain.dto.order.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderFulfillmentService {

    Mono<Order> get(UUID orderId);

    Mono<Order> complete(UUID orderId);

    Mono<Order> cancel(UUID orderId);

}
