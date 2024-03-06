package net.greeta.stock.order.infrastructure.repository.orchestrator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderWorkflowStatusRepositoryJpa extends JpaRepository<OrderWorkflowStatusEntity, UUID> {

    Boolean existsByOrderId(UUID orderId);

    Optional<OrderWorkflowStatusEntity> findByOrderId(UUID orderId);

}
