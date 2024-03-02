package net.greeta.stock.order.infrastructure.message.orchestrator.impl;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.orchestrator.WorkflowStep;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class Workflow {

    private final WorkflowStep<?> firstStep;
    private WorkflowStep<?> lastStep;

    private Workflow(WorkflowStep<?> firstStep) {
        this.firstStep = firstStep;
        this.lastStep = firstStep;
    }

    public static Workflow startWith(WorkflowStep<?> step){
        return new Workflow(step);
    }

    public Workflow thenNext(WorkflowStep<?> newStep){
        this.lastStep.setNextStep(newStep);
        newStep.setPreviousStep(this.lastStep);
        this.lastStep = newStep;
        return this;
    }

    public Workflow doOnSuccess(Function<UUID, Request> function) {
        this.lastStep.setNextStep(id -> function.apply(id));
        return this;
    }

    public Workflow doOnFailure(Function<UUID, Request> function){
        this.firstStep.setPreviousStep(id -> function.apply(id));
        return this;
    }

    public WorkflowStep<?> getFirstStep() {
        return firstStep;
    }
}
