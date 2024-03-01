package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.inventory.InventoryResponse;
import net.greeta.stock.common.orchestrator.WorkflowStep;
import org.reactivestreams.Publisher;

public interface InventoryStep extends WorkflowStep<InventoryResponse> {

    @Override
    default Publisher<Request> process(InventoryResponse response) {
        return switch (response){
            case InventoryResponse.Deducted r -> this.onSuccess(r);
            case InventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(InventoryResponse.Deducted response);

    Publisher<Request> onFailure(InventoryResponse.Declined response);

}
