package net.greeta.stock.order.infrastructure.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "order_workflow_action")
public class OrderWorkflowActionEntity {

    @Id
    private UUID id;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private EventType action;
    private Timestamp createdAt;

}
