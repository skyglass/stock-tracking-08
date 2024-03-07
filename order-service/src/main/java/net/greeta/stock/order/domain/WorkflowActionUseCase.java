package net.greeta.stock.order.domain;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;
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
    public void trackRequest(UUID orderId, EventType eventType, RequestType requestType) {
        this.repository.save(EntityDtoMapper.requestToOrderWorkflowAction(orderId, eventType, requestType));
    }

    @Override
    public void trackResponse(UUID orderId, EventType eventType, ResponseType responseType, ResponseStatus responseStatus) {
        this.repository.save(EntityDtoMapper.responseToOrderWorkflowAction(orderId, eventType, responseType, responseStatus));
    }
}
