package net.greeta.stock.order.infrastructure.repository.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderCompensationStatus;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderWorkflowStatus;
import net.greeta.stock.order.domain.exception.OrderCompensationStatusNotFoundException;
import net.greeta.stock.order.domain.exception.OrderWorkflowStatusNotFoundException;
import net.greeta.stock.order.domain.port.OrderCompensationStatusRepository;
import net.greeta.stock.order.domain.port.OrderWorkflowStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderWorkflowStatusRepositoryAdapter implements OrderWorkflowStatusRepository {
    private final ObjectMapper mapper;

    private final OrderWorkflowStatusRepositoryJpa orderWorkflowStatusRepository;

    @Override
    public Boolean existsByOrderId(UUID orderId) {
        return orderWorkflowStatusRepository.existsByOrderId(orderId);
    }

    @Override
    public OrderWorkflowStatus findByOrderId(UUID orderId) {
        var orderWorkflowStatusEntity = orderWorkflowStatusRepository
                .findByOrderId(orderId).orElseThrow(() -> new OrderWorkflowStatusNotFoundException(orderId));
        return mapper.convertValue(orderWorkflowStatusEntity, OrderWorkflowStatus.class);
    }

    @Override
    public OrderWorkflowStatus save(OrderWorkflowStatus orderWorkflowStatus) {
        var entity = mapper.convertValue(orderWorkflowStatus, OrderWorkflowStatusEntity.class);
        return mapper.convertValue(orderWorkflowStatusRepository.save(entity), OrderWorkflowStatus.class);
    }
}
