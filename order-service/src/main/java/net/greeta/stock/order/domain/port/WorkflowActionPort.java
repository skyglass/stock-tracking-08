package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.order.domain.service.WorkflowActionRetriever;
import net.greeta.stock.order.domain.service.WorkflowActionTracker;

import java.util.List;
import java.util.UUID;

public interface WorkflowActionPort extends WorkflowActionRetriever, WorkflowActionTracker {

}
