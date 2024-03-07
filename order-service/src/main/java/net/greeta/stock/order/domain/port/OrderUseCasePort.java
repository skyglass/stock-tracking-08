package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;

import java.util.UUID;

public interface OrderUseCasePort {

  void updateOrderStatus(Order order, OrderStatus orderStatus);

  void trackRequestAction(UUID orderId, EventType eventType, RequestType requestType);

  void trackResponseAction(UUID orderId, EventType eventType, ResponseType responseType, ResponseStatus responseStatus);

  Order getOrder(UUID orderId);

  OrderDetails getOrderDetails(UUID orderId);

  void exportOutBoxEvent(Order order, EventType eventType, RequestType requestType);

}
