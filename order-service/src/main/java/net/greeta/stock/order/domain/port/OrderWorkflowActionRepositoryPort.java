package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;

import java.util.List;
import java.util.UUID;

public interface OrderWorkflowActionRepositoryPort {

    Boolean existsByOrderIdAndAction(UUID orderId, String action);

    List<OrderWorkflowAction> findByOrderIdOrderByCreatedAt(UUID orderId);

    OrderWorkflowAction save(OrderWorkflowAction orderWorkflowAction);
}
