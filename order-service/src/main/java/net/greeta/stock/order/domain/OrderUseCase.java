package net.greeta.stock.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.common.domain.dto.order.*;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.domain.service.OrderService;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderUseCase implements OrderUseCasePort {

  private final ObjectMapper mapper;

  private final OrderRepositoryPort orderRepository;

  private final OrderService orderService;

  private final WorkflowActionPort workflowAction;

  @Override
  @Transactional
  public UUID placeOrder(OrderRequest orderRequest) {
    var order = mapper.convertValue(orderRequest, Order.class);
    order.setCreatedAt(Timestamp.from(Instant.now()));
    order.setStatus(OrderStatus.PENDING);
    order.setId(UUID.randomUUID());
    orderRepository.saveOrder(order);
    orderRepository.exportOutBoxEvent(order);
    workflowAction.track(order.getId(), WorkflowAction.ORDER_CREATED);
    return order.getId();
  }

  @Override
  @Transactional
  public void updateOrderStatus(UUID orderId, WorkflowAction action, boolean success) {
    var order = orderRepository.findOrderById(orderId);
    if (order.isPresent()) {
      if (success) {
        order.get().setStatus(OrderStatus.COMPLETED);
      } else {
        order.get().setStatus(OrderStatus.CANCELLED);
      }
      orderRepository.saveOrder(order.get());
      workflowAction.track(orderId, action);
    }
  }

  @Override
  @Transactional
  public void trackAction(UUID orderId, WorkflowAction action) {
    var order = orderRepository.findOrderById(orderId);
    if (order.isPresent()) {
      workflowAction.track(orderId, action);
    }
  }

  @Override
  public Order getOrder(UUID orderId) {
    return orderRepository.findOrderById(orderId).orElseThrow(
            () -> new RuntimeException("Order with id %s not found".formatted(orderId)));
  }

  @Override
  public OrderDetails getOrderDetails(UUID orderId) {
    return orderService.getOrderDetails(orderId);
  }
}
