package net.greeta.stock.order.infrastructure.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryStep extends SagaStep {

}
