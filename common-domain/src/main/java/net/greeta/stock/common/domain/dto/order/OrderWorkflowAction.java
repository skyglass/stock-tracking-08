package net.greeta.stock.common.domain.dto.order;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderWorkflowAction(UUID id,
                                  UUID orderId,
                                  WorkflowAction action,
                                  Instant createdAt) {
}
