package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.Response;
import net.greeta.stock.common.messages.inventory.InventoryResponse;
import net.greeta.stock.common.messages.payment.PaymentResponse;
import net.greeta.stock.common.orchestrator.WorkflowOrchestrator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface OrderFulfillmentOrchestrator extends WorkflowOrchestrator {

    Publisher<Request> orderInitialRequests();

    @Override
    default Publisher<Request> orchestrate(Response response) {
        return switch (response) {
            case PaymentResponse r -> this.handle(r);
            case InventoryResponse r -> this.handle(r);
            default -> Mono.empty();
        };
    }

    Publisher<Request> handle(PaymentResponse response);

    Publisher<Request> handle(InventoryResponse response);

}
