package net.greeta.stock.common.domain.dto.workflow.orchestrator;

public enum StepType {
    COMPENSABLE,
    IDEMPOTENT,
    IRREVERSIBLE,
    WAITING;
}
