package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;

import java.util.List;
import java.util.UUID;

public interface WorkflowActionPort {

    List<OrderWorkflowAction> retrieve(UUID orderId);

    void track(UUID orderId, EventType action);
}
