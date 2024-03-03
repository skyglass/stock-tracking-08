package net.greeta.stock.order.infrastructure.repository;

import net.greeta.stock.common.domain.dto.workflow.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderWorkflowActionJpaRepository extends JpaRepository<OrderWorkflowActionEntity, UUID> {

    Boolean existsByOrderIdAndAction(UUID orderId, EventType action);

    List<OrderWorkflowActionEntity> findByOrderIdOrderByCreatedAt(UUID orderId);

}
