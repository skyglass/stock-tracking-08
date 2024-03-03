package net.greeta.stock.order.infrastructure.orchestrator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.StepType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaOrchestrator {

    private final InventoryStep inventoryStep;

    private final PaymentStep paymentStep;

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
       inventoryStep.setSuccessStatus(OrderStatus.INVENTORY_COMPLETED);
       inventoryStep.setFailureStatus(OrderStatus.INVENTORY_FAILED);
       inventoryStep.setRequestAction(EventType.INVENTORY_REQUEST_INITIATED);
       inventoryStep.setCompensateAction(EventType.INVENTORY_RESTORE_INITIATED);
       inventoryStep.setSuccessAction(EventType.INVENTORY_DEDUCTED);
       inventoryStep.setFailureAction(EventType.INVENTORY_DECLINED);
   }

   private void buildPaymentStep() {
       paymentStep.setPreviousStep(inventoryStep);
       paymentStep.setSuccessStatus(OrderStatus.COMPLETED);
       paymentStep.setFailureStatus(OrderStatus.CANCELLED);
       paymentStep.setRequestAction(EventType.PAYMENT_REQUEST_INITIATED);
       paymentStep.setCompensateAction(EventType.PAYMENT_REFUND_INITIATED);
       paymentStep.setSuccessAction(EventType.PAYMENT_PROCESSED);
       paymentStep.setFailureAction(EventType.PAYMENT_DECLINED);
   }

   public SagaStep getWorkflowStep(EventType eventType) {
       return stepMap.get(eventType.getStepType());
   }
}
