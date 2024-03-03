package net.greeta.stock.order.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.common.domain.dto.order.*;
import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.infrastructure.message.outbox.OutBox;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;
import net.greeta.stock.order.infrastructure.orchestrator.SagaOrchestrator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderUseCase implements OrderUseCasePort {

  private final ObjectMapper mapper;

  private final OrderRepositoryPort orderRepository;

  private final WorkflowActionPort workflowAction;

  private final OutBoxRepository outBoxRepository;

  private final SagaOrchestrator sagaOrchestrator;

  @Override
  @Transactional
  public UUID placeOrder(OrderRequest orderRequest) {
    var order = mapper.convertValue(orderRequest, Order.class);
    order.setCreatedAt(Timestamp.from(Instant.now()));
    order.setStatus(OrderStatus.PENDING);
    order.setId(UUID.randomUUID());
    sagaOrchestrator.handleEvent(order, EventType.INVENTORY_REQUEST_INITIATED);
    orderRepository.saveOrder(order);
    return order.getId();
  }

  @Override
  public void updateOrderStatus(Order order, OrderStatus orderStatus) {
    order.setStatus(orderStatus);
    orderRepository.saveOrder(order);
  }

  @Override
  @Transactional
  public void updateOrderStatus(UUID orderId, EventType action, boolean success) {
    var order = orderRepository.findOrderById(orderId);
    if (success) {
      order.setStatus(OrderStatus.COMPLETED);
    } else {
      order.setStatus(OrderStatus.CANCELLED);
    }
    orderRepository.saveOrder(order);
    workflowAction.track(orderId, action);
  }

  @Override
  @Transactional
  public void trackAction(UUID orderId, EventType action) {
    workflowAction.track(orderId, action);
  }

  @Override
  public Order getOrder(UUID orderId) {
    return orderRepository.findOrderById(orderId);
  }

  @Override
  public OrderDetails getOrderDetails(UUID orderId) {
    Order order = orderRepository.findOrderById(orderId);
    List<OrderWorkflowAction> actions = workflowAction.retrieve(orderId);
    return new OrderDetails(order, actions);
  }

  @Override
  public void exportOutBoxEvent(Order order, EventType eventType) {
    var outbox =
            OutBox.builder()
                    .aggregateId(order.getId())
                    .aggregateType(AggregateType.ORDER)
                    .type(eventType)
                    .payload(mapper.convertValue(order, JsonNode.class))
                    .build();
    outBoxRepository.save(outbox);
  }
}
