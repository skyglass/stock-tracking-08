package net.greeta.stock.common.domain.workflow.messages.inventory;

import lombok.Builder;
import net.greeta.stock.common.domain.workflow.messages.Request;

import java.util.UUID;

public sealed interface InventoryRequest extends Request {

    @Builder
    record Deduct(UUID orderId,
                  UUID productId,
                  Integer quantity) implements InventoryRequest {

    }

    @Builder
    record Restore(UUID orderId) implements InventoryRequest {

    }

}
