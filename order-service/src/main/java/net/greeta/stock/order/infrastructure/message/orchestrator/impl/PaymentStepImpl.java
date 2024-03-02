package net.greeta.stock.order.infrastructure.message.orchestrator.impl;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.payment.PaymentResponse;
import net.greeta.stock.common.domain.workflow.orchestrator.RequestCompensator;
import net.greeta.stock.common.domain.workflow.orchestrator.RequestSender;
import net.greeta.stock.order.domain.service.OrderFulfillmentService;
import net.greeta.stock.order.domain.service.WorkflowActionTracker;
import net.greeta.stock.order.infrastructure.message.mapper.MessageDtoMapper;
import net.greeta.stock.order.infrastructure.message.orchestrator.PaymentStep;
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
    public Request compensate(UUID orderId) {
        this.tracker.track(orderId, WorkflowAction.PAYMENT_REFUND_INITIATED);
        this.previousStep.compensate(orderId);
        return MessageDtoMapper.toPaymentRefundRequest(orderId);
    }

    @Override
    public Request send(UUID orderId) {
        this.tracker.track(orderId, WorkflowAction.PAYMENT_REQUEST_INITIATED);
        Order order = this.service.get(orderId);
        return MessageDtoMapper.toPaymentProcessRequest(order);
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
    public Request onSuccess(PaymentResponse.Processed response) {
        this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_PROCESSED);
        return this.nextStep.send(response.orderId());
    }

    @Override
    public Request onFailure(PaymentResponse.Declined response) {
        this.tracker.track(response.orderId(), WorkflowAction.PAYMENT_DECLINED);
        return this.previousStep.compensate(response.orderId());
    }
}
