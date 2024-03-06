package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.workflow.orchestrator.OrderCompensationStatus;

import java.util.UUID;

public interface OrderCompensationStatusRepository {

    Boolean existsByOrderId(UUID orderId);

     OrderCompensationStatus findByOrderId(UUID orderId);

    OrderCompensationStatus save(OrderCompensationStatus orderCompensationStatus);
}
