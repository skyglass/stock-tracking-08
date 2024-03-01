package net.greeta.stock.order.application.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.order.common.service.OrderFulfillmentService;
import net.greeta.stock.order.infrastructure.repository.OrderRepositoryAdapter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final OrderRepositoryAdapter repository;

    @Override
    public Mono<Order> get(UUID orderId) {
        return Mono.just(this.repository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.get order with id %s not found".formatted(orderId)
                )));
    }

    @Override
    public Mono<Order> complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<Order> cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Mono<Order> update(UUID orderId, Consumer<Order> consumer) {
        return Mono.just(this.repository.findOrderByIdAndStatus(orderId, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.update order with id %s and status PENDING not found".formatted(orderId))
                ))
                .doOnNext(consumer)
                .map(this.repository::saveOrder);
    }

}
