package net.greeta.stock.common.domain.dto.workflow.orchestrator;

import net.greeta.stock.common.domain.dto.workflow.EventType;

public enum StepName {
    INVENTORY(EventType.INVENTORY, StepType.COMPENSABLE),
    PAYMENT(EventType.PAYMENT, StepType.COMPENSABLE);

    private EventType eventType;

    private StepType stepType;

    private StepName(EventType eventType, StepType stepType) {
        this.eventType = eventType;
        this.stepType = stepType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public StepType getStepType() {
        return stepType;
    }
}
