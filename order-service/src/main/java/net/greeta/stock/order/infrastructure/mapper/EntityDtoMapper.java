package net.greeta.stock.order.infrastructure.mapper;

import net.greeta.stock.common.domain.dto.order.*;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EntityDtoMapper {

    public static Order toOrder(OrderRequest request) {
        return Order.builder()
                            .status(OrderStatus.PENDING)
                            .customerId(request.customerId())
                            .productId(request.productId())
                            .quantity(request.quantity())
                            .price(request.price())
                            .build();
    }

    public static OrderWorkflowAction toOrderWorkflowAction(UUID orderId, EventType action) {
        return OrderWorkflowAction.builder()
                                  .orderId(orderId)
                                  .action(action)
                                  .createdAt(Instant.now())
                                  .build();
    }


    public static OrderDetails toOrderDetails(Order order, List<OrderWorkflowAction> actions) {
        return OrderDetails.builder()
                           .order(order)
                           .actions(actions)
                           .build();
    }

}
