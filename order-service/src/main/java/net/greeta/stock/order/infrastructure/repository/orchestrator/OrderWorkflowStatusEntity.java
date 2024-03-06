package net.greeta.stock.order.infrastructure.repository.orchestrator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.RequestStatus;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.StepName;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_workflow_status")
public class OrderWorkflowStatusEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID orderId;
    @Column(nullable = false)
    private UUID requestId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StepName stepName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus compensationStatus;
}
