package net.greeta.stock.order.application.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import net.greeta.stock.order.application.mapper.EntityDtoMapper;
import net.greeta.stock.order.common.service.OrderEventListener;
import net.greeta.stock.order.common.service.OrderService;
import net.greeta.stock.order.common.service.WorkflowActionRetriever;
import net.greeta.stock.order.infrastructure.repository.OrderRepositoryAdapter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryAdapter repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;

    @Override
    public Mono<Order> placeOrder(OrderRequest request) {
        var order = EntityDtoMapper.toOrder(request);
        return Mono.just(this.repository.saveOrder(order))
                              .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return Mono.just(this.repository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.update order with id %s and status PENDING not found".formatted(orderId))
                ))
                .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                .map(t -> EntityDtoMapper.toOrderDetails(t.getT1(), t.getT2()));
    }

}
