package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.order.WorkflowAction;

import java.util.List;
import java.util.UUID;

public interface OrderWorkflowActionRepositoryPort {

    Boolean existsByOrderIdAndAction(UUID orderId, WorkflowAction action);

    List<OrderWorkflowAction> findByOrderIdOrderByCreatedAt(UUID orderId);

    OrderWorkflowAction save(OrderWorkflowAction orderWorkflowAction);
}
