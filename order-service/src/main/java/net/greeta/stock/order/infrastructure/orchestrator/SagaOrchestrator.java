package net.greeta.stock.order.infrastructure.orchestrator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.StepType;
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

    private static final Map<StepType, SagaStep> stepMap = new HashMap<>();

   @PostConstruct
   public void init() {
        stepMap.put(StepType.INVENTORY, inventoryStep);
        stepMap.put(StepType.PAYMENT, paymentStep);
        buildInventoryStep();
        buildPaymentStep();
   }

   private void buildInventoryStep() {
       inventoryStep.setNextStep(paymentStep);
       inventoryStep.setSuccessResponseStatus(OrderStatus.INVENTORY_COMPLETED);
       inventoryStep.setFailureResponseStatus(OrderStatus.INVENTORY_FAILED);
       inventoryStep.setRequestAction(EventType.INVENTORY_REQUEST_INITIATED);
       inventoryStep.setCompensateRequestAction(EventType.INVENTORY_RESTORE_INITIATED);
       inventoryStep.setSuccessResponseEvent(EventType.INVENTORY_DEDUCTED);
       inventoryStep.setFailureResponseEvent(EventType.INVENTORY_DECLINED);
   }

   private void buildPaymentStep() {
       paymentStep.setPreviousStep(inventoryStep);
       paymentStep.setSuccessResponseStatus(OrderStatus.COMPLETED);
       paymentStep.setFailureResponseStatus(OrderStatus.CANCELLED);
       paymentStep.setRequestAction(EventType.PAYMENT_REQUEST_INITIATED);
       paymentStep.setCompensateRequestAction(EventType.PAYMENT_REFUND_INITIATED);
       paymentStep.setSuccessResponseEvent(EventType.PAYMENT_PROCESSED);
       paymentStep.setFailureResponseEvent(EventType.PAYMENT_DECLINED);
   }

    public void handleEvent(UUID orderId, EventType eventType) {
        Order order = orderRepository.findOrderById(orderId);
        handleEvent(order, eventType);
    }

    public void handleEvent(Order order, EventType eventType) {
        SagaStep sagaStep = stepMap.get(eventType.getStepType());
        sagaStep.handleEvent(order, eventType);
    }

}
