package net.greeta.stock.common.domain.dto.workflow.orchestrator;

import lombok.Builder;
import net.greeta.stock.common.domain.dto.workflow.StepName;

import java.util.UUID;

@Builder
public record OrderWorkflowStatus(UUID id,
                                      UUID orderId,
                                      UUID requestId,
                                      StepName stepName,
                                      RequestStatus requestStatus,
                                      RequestStatus compensationStatus) {
}
