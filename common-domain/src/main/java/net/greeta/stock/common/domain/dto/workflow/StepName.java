package net.greeta.stock.common.domain.dto.workflow;

public enum StepName {
    INVENTORY(StepType.COMPENSABLE),
    PAYMENT(StepType.COMPENSABLE);

    private StepType stepType;

    private StepName(StepType stepType) {
        this.stepType = stepType;
    }

    public StepType getStepType() {
        return stepType;
    }
}
