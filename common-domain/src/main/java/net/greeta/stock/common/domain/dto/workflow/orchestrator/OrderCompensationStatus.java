package net.greeta.stock.common.domain.dto.workflow.orchestrator;

import lombok.Builder;
import net.greeta.stock.common.domain.dto.workflow.EventType;

import java.util.UUID;

@Builder
public record OrderCompensationStatus(UUID id,
                                  UUID orderId,
                                  UUID requestId,
                                  EventType stepName,
                                  RequestStatus requestStatus) {
}

