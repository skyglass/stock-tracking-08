package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

  Optional<Order> findOrderById(UUID orderId);

  Optional<Order> findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus);

  Order saveOrder(Order order);

  void exportOutBoxEvent(Order order);
}
