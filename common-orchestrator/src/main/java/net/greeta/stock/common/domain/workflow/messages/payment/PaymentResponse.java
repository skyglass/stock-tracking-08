package net.greeta.stock.common.domain.workflow.messages.payment;

import lombok.Builder;
import net.greeta.stock.common.domain.workflow.messages.Response;

import java.util.UUID;

public sealed interface PaymentResponse extends Response {

    @Builder
    record Processed(UUID orderId,
                     UUID paymentId,
                     Integer customerId,
                     Integer amount) implements PaymentResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements PaymentResponse {

    }

}
