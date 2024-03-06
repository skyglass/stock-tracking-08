package net.greeta.stock.order.infrastructure.mapper;

import net.greeta.stock.common.domain.dto.order.*;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EntityDtoMapper {

    public static OrderWorkflowAction toOrderWorkflowAction(UUID orderId, EventType action) {
        return OrderWorkflowAction.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .action(action)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }


}
