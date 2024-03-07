package net.greeta.stock.order.infrastructure.mapper;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class EntityDtoMapper {

    public static OrderWorkflowAction requestToOrderWorkflowAction(UUID orderId,
                                                            EventType eventType,
                                                            RequestType requestType) {
        StringBuilder sb = new StringBuilder();
        sb.append(eventType.name());
        if (requestType == RequestType.COMPENSATE) {
            sb.append("_COMPENSATE");
        }
        sb.append("_REQUEST_STARTED");
        var action = sb.toString();
        return OrderWorkflowAction.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .action(action)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }

    public static OrderWorkflowAction responseToOrderWorkflowAction(UUID orderId,
                                                            EventType eventType,
                                                            ResponseType responseType,
                                                            ResponseStatus responseStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append(eventType.name());
        if (responseType == ResponseType.COMPENSATE) {
            sb.append("_COMPENSATE");
        }
        sb.append("_RESPONSE");
        if (responseStatus == ResponseStatus.SUCCESS) {
            sb.append("_SUCCESS");
        } else if (responseStatus == ResponseStatus.FAILURE) {
            sb.append("_FAILURE");
        }
        var action = sb.toString();
        return OrderWorkflowAction.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .action(action)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }


}
