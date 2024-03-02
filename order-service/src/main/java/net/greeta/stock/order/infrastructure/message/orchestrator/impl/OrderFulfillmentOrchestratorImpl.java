package net.greeta.stock.order.infrastructure.message.orchestrator.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.inventory.InventoryResponse;
import net.greeta.stock.common.domain.workflow.messages.payment.PaymentResponse;
import net.greeta.stock.order.domain.service.OrderFulfillmentService;
import net.greeta.stock.order.infrastructure.message.orchestrator.InventoryStep;
import net.greeta.stock.order.infrastructure.message.orchestrator.OrderFulfillmentOrchestrator;
import net.greeta.stock.order.infrastructure.message.orchestrator.PaymentStep;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentOrchestratorImpl implements OrderFulfillmentOrchestrator {

    private final PaymentStep paymentStep;
    private final InventoryStep inventoryStep;
    private final OrderFulfillmentService service;
    private Workflow workflow;

    @PostConstruct
    private void init() {
        this.workflow = Workflow.startWith(paymentStep)
                                .thenNext(inventoryStep)
                                .doOnFailure(id -> this.service.cancel(id))
                                .doOnSuccess(id -> this.service.complete(id));
    }

    @Override
    public Request orderInitialRequests() {
        return this.eventPublisher.publish()
                                  .flatMap(this.workflow.getFirstStep()::send);
    }

    @Override
    public Request handle(PaymentResponse response) {
        return this.paymentStep.process(response);
    }

    @Override
    public Request handle(InventoryResponse response) {
        return this.inventoryStep.process(response);
    }

}
