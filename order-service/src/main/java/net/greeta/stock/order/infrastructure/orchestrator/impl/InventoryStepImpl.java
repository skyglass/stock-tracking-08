package net.greeta.stock.order.infrastructure.orchestrator.impl;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.WorkflowAction;
import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.inventory.InventoryResponse;
import net.greeta.stock.common.orchestrator.RequestCompensator;
import net.greeta.stock.common.orchestrator.RequestSender;
import net.greeta.stock.order.common.service.OrderFulfillmentService;
import net.greeta.stock.order.common.service.WorkflowActionTracker;
import net.greeta.stock.order.infrastructure.message.mapper.MessageDtoMapper;
import net.greeta.stock.order.infrastructure.orchestrator.InventoryStep;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryStepImpl implements InventoryStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_RESTORE_INITIATED)
                           .<Request>thenReturn(MessageDtoMapper.toInventoryRestoreRequest(orderId))
                           .concatWith(this.previousStep.compensate(orderId));
    }

    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_REQUEST_INITIATED)
                           .then(this.service.get(orderId))
                           .map(MessageDtoMapper::toInventoryDeductRequest);
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
    public Publisher<Request> onSuccess(InventoryResponse.Deducted response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DEDUCTED)
                           .thenMany(this.nextStep.send(response.orderId()));
        // also Mono.from(...) can be used if we know for sure it is going to be only one request
    }

    @Override
    public Publisher<Request> onFailure(InventoryResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DECLINED)
                           .thenMany(this.previousStep.compensate(response.orderId()));
    }
}
