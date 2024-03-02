package net.greeta.stock.order.infrastructure.message.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.Response;
import net.greeta.stock.common.domain.workflow.messages.inventory.InventoryResponse;
import net.greeta.stock.common.domain.workflow.messages.payment.PaymentResponse;
import net.greeta.stock.common.domain.workflow.orchestrator.WorkflowOrchestrator;

public interface OrderFulfillmentOrchestrator extends WorkflowOrchestrator {

    Request orderInitialRequests();

    @Override
    default Request orchestrate(Response response) {
        return switch (response) {
            case PaymentResponse r -> this.handle(r);
            case InventoryResponse r -> this.handle(r);
            default -> null;
        };
    }

    Request handle(PaymentResponse response);

    Request handle(InventoryResponse response);

}
