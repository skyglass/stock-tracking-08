package net.greeta.stock.order.domain.service;

import net.greeta.stock.common.domain.dto.WorkflowAction;

import java.util.UUID;

public interface WorkflowActionTracker {

    void track(UUID orderId, WorkflowAction action);

}
