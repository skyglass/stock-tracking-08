package net.greeta.stock.order.common.service;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface WorkflowActionRetriever {

    Flux<OrderWorkflowAction> retrieve(UUID orderId);

}
