package net.greeta.stock.order.infrastructure.message.orchestrator.impl;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.inventory.InventoryResponse;
import net.greeta.stock.common.domain.workflow.orchestrator.RequestCompensator;
import net.greeta.stock.common.domain.workflow.orchestrator.RequestSender;
import net.greeta.stock.order.domain.service.OrderFulfillmentService;
import net.greeta.stock.order.domain.service.WorkflowActionTracker;
import net.greeta.stock.order.infrastructure.message.mapper.MessageDtoMapper;
import net.greeta.stock.order.infrastructure.message.orchestrator.InventoryStep;
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
    public Request compensate(UUID orderId) {
        this.tracker.track(orderId, WorkflowAction.INVENTORY_RESTORE_INITIATED);
        this.previousStep.compensate(orderId);
        return MessageDtoMapper.toInventoryRestoreRequest(orderId);
    }

    @Override
    public Request send(UUID orderId) {
        this.tracker.track(orderId, WorkflowAction.INVENTORY_REQUEST_INITIATED);
        Order order = this.service.get(orderId);
        return MessageDtoMapper.toInventoryDeductRequest(order);
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
    public Request onSuccess(InventoryResponse.Deducted response) {
        this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DEDUCTED);
        this.nextStep.send(response.orderId());
    }

    @Override
    public Request onFailure(InventoryResponse.Declined response) {
        this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DECLINED);
        this.previousStep.compensate(response.orderId());
    }
}
