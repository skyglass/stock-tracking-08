package net.greeta.stock.order.domain;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.infrastructure.mapper.EntityDtoMapper;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionRepositoryAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowActionUseCase implements WorkflowActionPort {

    private final OrderWorkflowActionRepositoryAdapter repository;

    @Override
    public List<OrderWorkflowAction> retrieve(UUID orderId) {
        return this.repository.findByOrderIdOrderByCreatedAt(orderId);
    }

    @Override
    public void track(UUID orderId, EventType action) {
        this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action));
    }
}
