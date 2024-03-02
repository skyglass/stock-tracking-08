package net.greeta.stock.order.infrastructure.message.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.inventory.InventoryResponse;
import net.greeta.stock.common.domain.workflow.orchestrator.WorkflowStep;

public interface InventoryStep extends WorkflowStep<InventoryResponse> {

    @Override
    default Request process(InventoryResponse response) {
        return switch (response){
            case InventoryResponse.Deducted r -> this.onSuccess(r);
            case InventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Request onSuccess(InventoryResponse.Deducted response);

    Request onFailure(InventoryResponse.Declined response);

}
