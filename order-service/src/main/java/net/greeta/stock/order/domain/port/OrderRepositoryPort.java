package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

  Order findOrderById(UUID orderId);

  Order findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus);

  Order saveOrder(Order order);

}
