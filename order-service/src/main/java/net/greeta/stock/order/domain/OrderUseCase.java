package net.greeta.stock.order.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.common.domain.dto.order.*;
import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.order.domain.port.WorkflowActionPort;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.domain.port.OrderUseCasePort;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.infrastructure.message.outbox.OutBox;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderUseCase implements OrderUseCasePort {

  private final ObjectMapper mapper;

  private final OrderRepositoryPort orderRepository;

  private final WorkflowActionPort workflowAction;

  private final OutBoxRepository outBoxRepository;

  @Override
  @Transactional
  public void updateOrderStatus(Order order, OrderStatus orderStatus) {
    log.info("OrderUseCase.updateOrderStatus for order {} and status {}", order.getId(), orderStatus);
    order.setStatus(orderStatus);
    orderRepository.saveOrder(order);
  }

  @Override
  @Transactional
  public void trackAction(UUID orderId, EventType action) {
    workflowAction.track(orderId, action);
  }

  @Override
  @Transactional(readOnly = true)
  public Order getOrder(UUID orderId) {
    return orderRepository.findOrderById(orderId);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDetails getOrderDetails(UUID orderId) {
    Order order = orderRepository.findOrderById(orderId);
    List<OrderWorkflowAction> actions = workflowAction.retrieve(orderId);
    return new OrderDetails(order, actions);
  }

  @Override
  @Transactional
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
