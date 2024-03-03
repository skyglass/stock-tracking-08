package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;

import java.util.List;
import java.util.UUID;

public interface OrderWorkflowActionRepositoryPort {

    Boolean existsByOrderIdAndAction(UUID orderId, EventType action);

    List<OrderWorkflowAction> findByOrderIdOrderByCreatedAt(UUID orderId);

    OrderWorkflowAction save(OrderWorkflowAction orderWorkflowAction);
}
