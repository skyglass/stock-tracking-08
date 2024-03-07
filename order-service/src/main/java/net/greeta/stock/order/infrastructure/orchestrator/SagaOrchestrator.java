package net.greeta.stock.order.infrastructure.orchestrator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;
import net.greeta.stock.order.domain.port.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaOrchestrator {

    private final InventoryStep inventoryStep;

    private final PaymentStep paymentStep;

    private final OrderRepositoryPort orderRepository;

    private static final Map<EventType, SagaStep> stepMap = new HashMap<>();

   @PostConstruct
   public void init() {
        stepMap.put(EventType.INVENTORY, inventoryStep);
        stepMap.put(EventType.PAYMENT, paymentStep);
        buildInventoryStep();
        buildPaymentStep();
   }

   private void buildInventoryStep() {
       inventoryStep.setNextStep(paymentStep);
       inventoryStep.setSuccessResponseStatus(OrderStatus.INVENTORY_COMPLETED);
       inventoryStep.setFailureResponseStatus(OrderStatus.INVENTORY_FAILED);
   }

   private void buildPaymentStep() {
       paymentStep.setPreviousStep(inventoryStep);
       paymentStep.setSuccessResponseStatus(OrderStatus.COMPLETED);
       paymentStep.setFailureResponseStatus(OrderStatus.PAYMENT_FAILED);
   }

    public void handleRequestEvent(UUID orderId, EventType eventType) {
        Order order = orderRepository.findOrderById(orderId);
        handleRequestEvent(order, eventType);
    }

    public void handleResponseEvent(UUID orderId,
                                    EventType eventType,
                                    ResponseType responseType,
                                    ResponseStatus responseStatus) {
        Order order = orderRepository.findOrderById(orderId);
        handleResponseEvent(order, eventType, responseType, responseStatus);
    }

    public void handleRequestEvent(Order order, EventType eventType) {
        SagaStep sagaStep = stepMap.get(eventType);
        sagaStep.handleRequest(order, RequestType.ACTION);
    }

    public void handleResponseEvent(Order order,
                                    EventType eventType,
                                    ResponseType responseType,
                                    ResponseStatus responseStatus) {
        SagaStep sagaStep = stepMap.get(eventType);
        sagaStep.handleResponse(order, responseType, responseStatus);
    }

}
