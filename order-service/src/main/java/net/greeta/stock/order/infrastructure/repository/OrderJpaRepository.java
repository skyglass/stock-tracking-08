package net.greeta.stock.order.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<Order> findByIdAndStatus(UUID orderId, OrderStatus status);

}
