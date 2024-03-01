package net.greeta.stock.order.infrastructure.orchestrator.impl;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.WorkflowAction;
import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.payment.PaymentResponse;
import net.greeta.stock.common.orchestrator.RequestCompensator;
import net.greeta.stock.common.orchestrator.RequestSender;
import net.greeta.stock.order.common.service.OrderFulfillmentService;
import net.greeta.stock.order.common.service.WorkflowActionTracker;
import net.greeta.stock.order.infrastructure.message.mapper.MessageDtoMapper;
import net.greeta.stock.order.infrastructure.orchestrator.PaymentStep;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentStepImpl implements PaymentStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.PAYMENT_REFUND_INITIATED)
                           .<Request>thenReturn(MessageDtoMapper.toPaymentRefundRequest(orderId))
                           .concatWith(this.previousStep.compensate(orderId));
    }

    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.PAYMENT_REQUEST_INITIATED)
                           .then(this.service.get(orderId))
                           .map(MessageDtoMapper::toPaymentProcessRequest);
    }

    @Override
    public void setPreviousStep(RequestCompensator previousStep) {
        this.previousStep = previousStep;
    }

    @Override
    public void setNextStep(RequestSender nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public Publisher<Request> onSuccess(PaymentResponse.Processed response) {
        return this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_PROCESSED)
                           .thenMany(this.nextStep.send(response.orderId()));
        // also Mono.from(...) can be used if we know for sure it is going to be only one request
    }

    @Override
    public Publisher<Request> onFailure(PaymentResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_DECLINED)
                           .thenMany(this.previousStep.compensate(response.orderId()));
    }
}
