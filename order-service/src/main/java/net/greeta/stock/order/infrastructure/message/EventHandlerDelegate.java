package net.greeta.stock.order.infrastructure.message;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import io.micrometer.common.util.StringUtils;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;
import net.greeta.stock.order.domain.PlacedOrderEvent;
import net.greeta.stock.order.infrastructure.orchestrator.SagaOrchestrator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.order.infrastructure.message.log.MessageLog;
import net.greeta.stock.order.infrastructure.message.log.MessageLogRepository;

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
            var eventType = getEventTypeHeaderAsEnum(event.getHeaders(), "eventType");
            var responseType = getResponseTypeHeaderAsEnum(event.getHeaders(), "responseType");
            var responseStatus = getResponseStatusHeaderAsEnum(event.getHeaders(), "responseStatus");
            var exceptionType = getHeaderAsString(event.getHeaders(), "exceptionType");
            var exceptionMessage = getHeaderAsString(event.getHeaders(), "exceptionMessage");
            if (StringUtils.isNotBlank(exceptionType)) {
                log.info("EventHandlerDelegate.reserveCustomerBalanceStage handle failure response for order {}, " +
                                "event {}, exceptionType {} and exceptionMessage {}",
                        placedOrderEvent.id(), eventType, exceptionType, exceptionMessage);
            } else {
                log.info("EventHandlerDelegate.reserveCustomerBalanceStage for order {} and event {}",
                        placedOrderEvent.id(), eventType);
            }
            sagaOrchestrator.handleResponseEvent(placedOrderEvent.id(), eventType, responseType, responseStatus);
            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    @Transactional
    public void reserveProductStockStage(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());
            var eventType = getEventTypeHeaderAsEnum(event.getHeaders(), "eventType");
            var responseType = getResponseTypeHeaderAsEnum(event.getHeaders(), "responseType");
            var responseStatus = getResponseStatusHeaderAsEnum(event.getHeaders(), "responseStatus");
            var exceptionType = getHeaderAsString(event.getHeaders(), "exceptionType");
            var exceptionMessage = getHeaderAsString(event.getHeaders(), "exceptionMessage");
            if (StringUtils.isNotBlank(exceptionType)) {
                log.info("EventHandlerDelegate.reserveProductStockStage handle failure response for order {}, " +
                                "event {}, exceptionType {} and exceptionMessage {}",
                        placedOrderEvent.id(), eventType, exceptionType, exceptionMessage);
            } else {
                log.info("EventHandlerDelegate.reserveProductStockStage for order {} and event {}",
                        placedOrderEvent.id(), eventType);
            }
            sagaOrchestrator.handleResponseEvent(placedOrderEvent.id(), eventType, responseType, responseStatus);
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

    private EventType getEventTypeHeaderAsEnum(MessageHeaders headers, String name) {
        String stringResult = getHeaderAsString(headers, name, true);
        return EventType.valueOf(stringResult);
    }

    private ResponseType getResponseTypeHeaderAsEnum(MessageHeaders headers, String name) {
        String stringResult = getHeaderAsString(headers, name, true);
        return ResponseType.valueOf(stringResult);
    }

    private ResponseStatus getResponseStatusHeaderAsEnum(MessageHeaders headers, String name) {
        String stringResult = getHeaderAsString(headers, name, true);
        return ResponseStatus.valueOf(stringResult);
    }

    private String getHeaderAsString(MessageHeaders headers, String name) {
        return getHeaderAsString(headers, name, false);
    }

    private String getHeaderAsString(MessageHeaders headers, String name, boolean isRequired) {
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value) && isRequired) {
            throw new IllegalArgumentException(
                    String.format("Expected record header %s not present", name));
        }
        if (Objects.isNull(value)) {
            return null;
        }
        return new String(value, StandardCharsets.UTF_8);
    }
}
