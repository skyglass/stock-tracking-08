package net.greeta.stock.order.infrastructure.message;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import net.greeta.stock.common.domain.dto.AggregateType;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import net.greeta.stock.order.domain.PlacedOrderEvent;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.order.infrastructure.message.log.MessageLog;
import net.greeta.stock.order.infrastructure.message.log.MessageLogRepository;
import net.greeta.stock.order.infrastructure.message.outbox.OutBox;
import net.greeta.stock.order.infrastructure.message.outbox.OutBoxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlerDelegate {

    private final ObjectMapper mapper;

    private final OrderUseCasePort orderUseCase;

    private final MessageLogRepository messageLogRepository;

    private final OutBoxRepository outBoxRepository;

    @Transactional
    public void reserveCustomerBalanceStage(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());
            var eventType = getHeaderAsEnum(event.getHeaders(), "eventType");
            if (eventType == WorkflowAction.PAYMENT_PROCESSED) {
                var outbox = OutBox.builder()
                        .aggregateId(placedOrderEvent.id())
                        .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                        .aggregateType(AggregateType.ORDER)
                        .type(WorkflowAction.PAYMENT_PROCESSED)
                        .build();
                outBoxRepository.save(outbox);
                orderUseCase.trackAction(placedOrderEvent.id(), WorkflowAction.PAYMENT_PROCESSED);
            } else if (eventType == WorkflowAction.PAYMENT_DECLINED) {
                orderUseCase.updateOrderStatus(placedOrderEvent.id(),
                        WorkflowAction.PAYMENT_DECLINED, false);
            }

            // Marked message is processed
            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    @Transactional
    public void reserveProductStockStage(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());
            var eventType = getHeaderAsEnum(event.getHeaders(), "eventType");
            if (eventType == WorkflowAction.INVENTORY_DEDUCTED) {
                orderUseCase.updateOrderStatus(placedOrderEvent.id(), WorkflowAction.INVENTORY_DEDUCTED, true);
            } else if (eventType == WorkflowAction.INVENTORY_DECLINED) {
                orderUseCase.updateOrderStatus(placedOrderEvent.id(), WorkflowAction.INVENTORY_DECLINED, false);
                var outbox = OutBox.builder()
                        .aggregateId(placedOrderEvent.id())
                        .aggregateType(AggregateType.ORDER)
                        .type(WorkflowAction.PAYMENT_REFUND_INITIATED)
                        .payload(mapper.convertValue(placedOrderEvent, JsonNode.class))
                        .build();
                outBoxRepository.save(outbox);
                orderUseCase.trackAction(placedOrderEvent.id(), WorkflowAction.PAYMENT_REFUND_INITIATED);
            }

            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    private PlacedOrderEvent deserialize(String event) {
        PlacedOrderEvent placedOrderEvent;
        try {
            placedOrderEvent = mapper.readValue(event, PlacedOrderEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }
        return placedOrderEvent;
    }

    private WorkflowAction getHeaderAsEnum(MessageHeaders headers, String name) {
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(
                    String.format("Expected record header %s not present", name));
        }
        String stringResult = new String(value, StandardCharsets.UTF_8);
        return WorkflowAction.valueOf(stringResult);
    }
}
