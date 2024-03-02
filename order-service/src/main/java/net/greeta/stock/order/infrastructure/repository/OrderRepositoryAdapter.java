package net.greeta.stock.order.infrastructure.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.common.domain.dto.AggregateType;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.infrastructure.message.outbox.OutBox;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

  private final ObjectMapper mapper;

  private final OrderJpaRepository orderJpaRepository;

  private final OutBoxRepository outBoxRepository;

  @Override
  public Optional<Order> findOrderById(UUID orderId) {
    return orderJpaRepository
        .findById(orderId)
        .map(orderEntity -> mapper.convertValue(orderEntity, Order.class));
  }

  @Override
  public Optional<Order> findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus) {
    return orderJpaRepository
            .findByIdAndStatus(orderId, orderStatus)
            .map(orderEntity -> mapper.convertValue(orderEntity, Order.class));
  }

  @Override
  public Order saveOrder(Order order) {
    var entity = mapper.convertValue(order, OrderEntity.class);
    return mapper.convertValue(orderJpaRepository.save(entity), Order.class);
  }

  @Override
  public void exportOutBoxEvent(Order order) {
    var outbox =
        OutBox.builder()
            .aggregateId(order.getId())
            .aggregateType(AggregateType.ORDER)
            .type(WorkflowAction.ORDER_CREATED)
            .payload(mapper.convertValue(order, JsonNode.class))
            .build();
    outBoxRepository.save(outbox);
  }
}
