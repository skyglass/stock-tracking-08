package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.payment.PaymentResponse;
import net.greeta.stock.common.orchestrator.WorkflowStep;
import org.reactivestreams.Publisher;

public interface PaymentStep extends WorkflowStep<PaymentResponse> {

    @Override
    default Publisher<Request> process(PaymentResponse response) {
        return switch (response){
            case PaymentResponse.Processed r -> this.onSuccess(r);
            case PaymentResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(PaymentResponse.Processed response);

    Publisher<Request> onFailure(PaymentResponse.Declined response);

}
