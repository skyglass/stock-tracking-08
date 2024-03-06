package net.greeta.stock.common.domain.dto.order;

import lombok.Builder;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
public record OrderWorkflowAction(UUID id,
                                  UUID orderId,
                                  EventType action,
                                  Timestamp createdAt) {
}
