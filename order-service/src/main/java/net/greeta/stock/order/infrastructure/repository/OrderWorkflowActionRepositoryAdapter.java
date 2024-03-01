package net.greeta.stock.order.infrastructure.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.order.WorkflowAction;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.domain.port.OrderWorkflowActionRepositoryPort;
import net.greeta.stock.order.infrastructure.message.EventHandlerAdapter;
import net.greeta.stock.order.infrastructure.message.outbox.OutBox;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderWorkflowActionRepositoryAdapter implements OrderWorkflowActionRepositoryPort {

    private final ObjectMapper mapper;

    private final OrderWorkflowActionJpaRepository orderWorkflowActionJpaRepository;

    @Override
    public Boolean existsByOrderIdAndAction(UUID orderId, WorkflowAction action) {
        return orderWorkflowActionJpaRepository.existsByOrderIdAndAction(orderId, action);
    }

    @Override
    public List<OrderWorkflowAction> findByOrderIdOrderByCreatedAt(UUID orderId) {
        return orderWorkflowActionJpaRepository
                .findByOrderIdOrderByCreatedAt(orderId)
                .stream()
                .map(entity -> mapper.convertValue(entity, OrderWorkflowAction.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderWorkflowAction save(OrderWorkflowAction orderWorkflowAction) {
        var entity = mapper.convertValue(orderWorkflowAction, OrderWorkflowActionEntity.class);
        return mapper.convertValue(orderWorkflowActionJpaRepository.save(entity), OrderWorkflowAction.class);
    }

}