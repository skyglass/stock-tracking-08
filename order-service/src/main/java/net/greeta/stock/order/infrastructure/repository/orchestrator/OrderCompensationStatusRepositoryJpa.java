package net.greeta.stock.order.infrastructure.repository.orchestrator;

import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderCompensationStatusRepositoryJpa extends JpaRepository<OrderCompensationStatusEntity, UUID> {

    Boolean existsByOrderId(UUID orderId);

    Optional<OrderCompensationStatusEntity> findByOrderId(UUID orderId);

}
