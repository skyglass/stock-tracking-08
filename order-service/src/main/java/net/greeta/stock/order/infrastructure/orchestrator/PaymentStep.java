package net.greeta.stock.order.infrastructure.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStep extends SagaStep {

}
