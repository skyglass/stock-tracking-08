package net.greeta.stock.order.application.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.order.infrastructure.mapper.EntityDtoMapper;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.domain.service.OrderEventListener;
import net.greeta.stock.order.domain.service.OrderService;
import net.greeta.stock.order.infrastructure.repository.OrderRepositoryAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryAdapter repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionPort actionRetriever;

    @Override
    @Transactional
    public Order placeOrder(OrderRequest request) {
        var order = EntityDtoMapper.toOrder(request);
        var result = this.repository.saveOrder(order);
        eventListener.emitOrderCreated(order);
        return result;
    }

    @Override
    public OrderDetails getOrderDetails(UUID orderId) {
        Order order = this.repository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "OrderFulfillmentServiceImpl.update order with id %s and status PENDING not found".formatted(orderId))
                );
        List<OrderWorkflowAction> actions = this.actionRetriever.retrieve(orderId);
        return new OrderDetails(order, actions);
    }

}
