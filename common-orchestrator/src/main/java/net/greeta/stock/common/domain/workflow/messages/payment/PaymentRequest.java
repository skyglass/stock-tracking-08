package net.greeta.stock.common.domain.workflow.messages.payment;

import lombok.Builder;
import net.greeta.stock.common.domain.workflow.messages.Request;

import java.util.UUID;

public sealed interface PaymentRequest extends Request {


    @Builder
    record Process(UUID orderId,
                   UUID customerId,
                   Integer amount) implements PaymentRequest {
    }

    @Builder
    record Refund(UUID orderId) implements PaymentRequest {
    }

}
