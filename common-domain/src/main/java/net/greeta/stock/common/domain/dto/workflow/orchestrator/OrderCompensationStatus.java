package net.greeta.stock.common.domain.dto.workflow.orchestrator;

import com.sun.net.httpserver.Request;
import lombok.Builder;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.StepName;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
public record OrderCompensationStatus(UUID id,
                                  UUID orderId,
                                  UUID requestId,
                                  StepName stepName,
                                  RequestStatus requestStatus) {
}

