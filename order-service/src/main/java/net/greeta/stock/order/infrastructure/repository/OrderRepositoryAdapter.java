package net.greeta.stock.order.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.order.domain.exception.OrderNotFoundException;
import net.greeta.stock.order.domain.exception.OrderWithStatusNotFoundException;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

  private final ObjectMapper mapper;

  private final OrderJpaRepository orderJpaRepository;

  @Override
  public Order findOrderById(UUID orderId) {
    var orderEntity = orderJpaRepository
        .findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    return mapper.convertValue(orderEntity, Order.class);
  }

  @Override
  public Order findOrderByIdAndStatus(UUID orderId, OrderStatus orderStatus) {
    var orderEntity = orderJpaRepository
            .findByIdAndStatus(orderId, orderStatus)
            .orElseThrow(() -> new OrderWithStatusNotFoundException(orderId, orderStatus));
    return mapper.convertValue(orderEntity, Order.class);
  }

  @Override
  public Order saveOrder(Order order) {
    var entity = mapper.convertValue(order, OrderEntity.class);
    return mapper.convertValue(orderJpaRepository.save(entity), Order.class);
  }
}
