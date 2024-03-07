package net.greeta.stock.order.domain.port;

import net.greeta.stock.common.domain.dto.order.OrderWorkflowAction;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;

import java.util.List;
import java.util.UUID;

public interface WorkflowActionPort {

    List<OrderWorkflowAction> retrieve(UUID orderId);

    void trackRequest(UUID orderId, EventType eventType, RequestType requestType);

    void trackResponse(UUID orderId, EventType eventType, ResponseType responseType, ResponseStatus responseStatus);
}
