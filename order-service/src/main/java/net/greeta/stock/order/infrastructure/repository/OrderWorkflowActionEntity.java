package net.greeta.stock.order.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWorkflowActionEntity {

    @Id
    private UUID id;
    private UUID orderId;
    private EventType action;
    private Instant createdAt;

}
