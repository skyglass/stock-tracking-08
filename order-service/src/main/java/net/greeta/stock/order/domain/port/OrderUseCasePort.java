package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.WorkflowAction;

import java.util.UUID;

public interface OrderUseCasePort {

  UUID placeOrder(OrderRequest orderRequest);

  void updateOrderStatus(UUID orderId, WorkflowAction action, boolean success);

  void trackAction(UUID orderId, WorkflowAction action);

  Order getOrder(UUID orderId);

  OrderDetails getOrderDetails(UUID orderId);

}
