package net.greeta.stock.order.domain.service;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;

import java.util.List;
import java.util.UUID;

public interface WorkflowActionRetriever {

    List<OrderWorkflowAction> retrieve(UUID orderId);

}
