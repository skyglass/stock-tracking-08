package net.greeta.stock.order.infrastructure.message;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.order.domain.PlacedOrderEvent;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import net.greeta.stock.order.infrastructure.orchestrator.SagaOrchestrator;
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

    private final MessageLogRepository messageLogRepository;

    private final SagaOrchestrator sagaOrchestrator;

    @Transactional
    public void reserveCustomerBalanceStage(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());
            var eventType = getHeaderAsEnum(event.getHeaders(), "eventType");
            sagaOrchestrator.handleEvent(placedOrderEvent.id(), eventType);
            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    @Transactional
    public void reserveProductStockStage(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());
            var eventType = getHeaderAsEnum(event.getHeaders(), "eventType");
            sagaOrchestrator.handleEvent(placedOrderEvent.id(), eventType);
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

    private EventType getHeaderAsEnum(MessageHeaders headers, String name) {
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(
                    String.format("Expected record header %s not present", name));
        }
        String stringResult = new String(value, StandardCharsets.UTF_8);
        return EventType.valueOf(stringResult);
    }
}
