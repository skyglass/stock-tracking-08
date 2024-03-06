package net.greeta.stock.order.infrastructure.repository.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderCompensationStatus;
import net.greeta.stock.order.domain.exception.OrderCompensationStatusNotFoundException;
import net.greeta.stock.order.domain.exception.OrderNotFoundException;
import net.greeta.stock.order.domain.port.OrderCompensationStatusRepository;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionEntity;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderCompensationStatusRepositoryAdapter implements OrderCompensationStatusRepository {
    private final ObjectMapper mapper;

    private final OrderCompensationStatusRepositoryJpa orderCompensationStatusRepository;

    @Override
    public Boolean existsByOrderId(UUID orderId) {
        return orderCompensationStatusRepository.existsByOrderId(orderId);
    }

    @Override
    public OrderCompensationStatus findByOrderId(UUID orderId) {
        var orderCompensationStatusEntity = orderCompensationStatusRepository
                .findByOrderId(orderId).orElseThrow(() -> new OrderCompensationStatusNotFoundException(orderId));
        return mapper.convertValue(orderCompensationStatusEntity, OrderCompensationStatus.class);
    }

    @Override
    public OrderCompensationStatus save(OrderCompensationStatus orderCompensationStatus) {
        var entity = mapper.convertValue(orderCompensationStatus, OrderCompensationStatusEntity.class);
        return mapper.convertValue(orderCompensationStatusRepository.save(entity), OrderCompensationStatus.class);
    }
}
