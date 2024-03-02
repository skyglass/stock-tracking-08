package net.greeta.stock.order.application.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.order.domain.service.OrderFulfillmentService;
import net.greeta.stock.order.infrastructure.repository.OrderRepositoryAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final OrderRepositoryAdapter repository;

    @Override
    public Order get(UUID orderId) {
        return this.repository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.get order with id %s not found".formatted(orderId)
                ));
    }

    @Override
    @Transactional
    public Order complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED));
    }

    @Override
    @Transactional
    public Order cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Order update(UUID orderId, Consumer<Order> consumer) {
        var order = this.repository.findOrderByIdAndStatus(orderId, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.update order with id %s and status PENDING not found".formatted(orderId))
                );
        consumer.accept(order);
        return this.repository.saveOrder(order);
    }

}
