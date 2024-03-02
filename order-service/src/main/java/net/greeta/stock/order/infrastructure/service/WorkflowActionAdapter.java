package net.greeta.stock.order.infrastructure.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.order.infrastructure.mapper.EntityDtoMapper;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionRepositoryAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowActionAdapter implements WorkflowActionPort {

    private final OrderWorkflowActionRepositoryAdapter repository;

    @Override
    public List<OrderWorkflowAction> retrieve(UUID orderId) {
        return this.repository.findByOrderIdOrderByCreatedAt(orderId);
    }

    @Override
    public void track(UUID orderId, WorkflowAction action) {
        this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action));
    }
}
