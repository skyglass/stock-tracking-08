package net.greeta.stock.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.domain.port.CreateOrderUseCasePort;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.infrastructure.orchestrator.SagaOrchestrator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderUseCasePort {

    private final ObjectMapper mapper;

    private final SagaOrchestrator sagaOrchestrator;

    private final OrderRepositoryPort orderRepository;

    @Override
    @Transactional
    public UUID placeOrder(OrderRequest orderRequest) {
        var order = mapper.convertValue(orderRequest, Order.class);
        order.setCreatedAt(Timestamp.from(Instant.now()));
        order.setStatus(OrderStatus.PENDING);
        order.setId(UUID.randomUUID());
        orderRepository.saveOrder(order);
        sagaOrchestrator.handleRequestEvent(order, EventType.INVENTORY_REQUEST_INITIATED);
        return order.getId();
    }
}
