package net.greeta.stock.order.application.service;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.order.WorkflowAction;
import net.greeta.stock.common.util.DuplicateEventValidator;
import net.greeta.stock.order.application.mapper.EntityDtoMapper;
import net.greeta.stock.order.common.service.WorkflowActionRetriever;
import net.greeta.stock.order.common.service.WorkflowActionTracker;
import net.greeta.stock.order.infrastructure.repository.OrderWorkflowActionRepositoryAdapter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowActionService implements WorkflowActionTracker, WorkflowActionRetriever {

    private final OrderWorkflowActionRepositoryAdapter repository;

    @Override
    public Flux<OrderWorkflowAction> retrieve(UUID orderId) {
        return Flux.fromIterable(this.repository.findByOrderIdOrderByCreatedAt(orderId));
    }

    @Override
    public Mono<Void> track(UUID orderId, WorkflowAction action) {
        return DuplicateEventValidator.validate(
                Mono.just(this.repository.existsByOrderIdAndAction(orderId, action)),
                Mono.just(this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action)))
        ).then();
    }
}
