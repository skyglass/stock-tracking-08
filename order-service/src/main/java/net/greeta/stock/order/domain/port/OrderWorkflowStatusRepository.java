package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderCompensationStatus;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderWorkflowStatus;

import java.util.UUID;

public interface OrderWorkflowStatusRepository {

    Boolean existsByOrderId(UUID orderId);

    OrderWorkflowStatus findByOrderId(UUID orderId);

    OrderWorkflowStatus save(OrderWorkflowStatus orderWorkflowStatus);
}
