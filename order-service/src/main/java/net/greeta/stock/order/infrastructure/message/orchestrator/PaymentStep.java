package net.greeta.stock.order.infrastructure.message.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.payment.PaymentResponse;
import net.greeta.stock.common.domain.workflow.orchestrator.WorkflowStep;

public interface PaymentStep extends WorkflowStep<PaymentResponse> {

    @Override
    default Request process(PaymentResponse response) {
        return switch (response){
            case PaymentResponse.Processed r -> this.onSuccess(r);
            case PaymentResponse.Declined r -> this.onFailure(r);
        };
    }

    Request onSuccess(PaymentResponse.Processed response);

    Request onFailure(PaymentResponse.Declined response);

}
