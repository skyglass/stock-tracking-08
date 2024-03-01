package net.greeta.stock.order.common.service;

import net.greeta.stock.common.domain.dto.order.WorkflowAction;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface WorkflowActionTracker {

    Mono<Void> track(UUID orderId, WorkflowAction action);

}
