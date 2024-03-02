package net.greeta.stock.common.domain.workflow.messages.inventory;

import lombok.Builder;
import net.greeta.stock.common.domain.workflow.messages.Response;

import java.util.UUID;

public sealed interface InventoryResponse extends Response {


    @Builder
    record Deducted(UUID orderId,
                     UUID inventoryId,
                     Integer productId,
                     Integer quantity) implements InventoryResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements InventoryResponse {

    }

}
