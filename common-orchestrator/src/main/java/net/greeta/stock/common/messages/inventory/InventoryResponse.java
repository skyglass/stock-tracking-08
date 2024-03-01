package net.greeta.stock.common.messages.inventory;

import net.greeta.stock.common.messages.Response;
import lombok.Builder;

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
