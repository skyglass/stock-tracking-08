package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;

import java.util.UUID;

public interface OrderUseCasePort {

  UUID placeOrder(OrderRequest orderRequest);

  void updateOrderStatus(Order order, OrderStatus orderStatus);

  void updateOrderStatus(UUID orderId, EventType action, boolean success);

  void trackAction(UUID orderId, EventType action);

  Order getOrder(UUID orderId);

  OrderDetails getOrderDetails(UUID orderId);

}
