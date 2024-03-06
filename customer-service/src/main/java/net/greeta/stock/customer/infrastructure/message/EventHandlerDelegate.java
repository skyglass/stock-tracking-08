package net.greeta.stock.customer.infrastructure.message;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;
import net.greeta.stock.customer.domain.port.CustomerUseCasePort;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greeta.stock.customer.domain.PlacedOrderEvent;
import net.greeta.stock.customer.infrastructure.message.log.MessageLog;
import net.greeta.stock.customer.infrastructure.message.log.MessageLogRepository;
import net.greeta.stock.customer.infrastructure.message.outbox.OutBox;
import net.greeta.stock.customer.infrastructure.message.outbox.OutBoxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlerDelegate {

    private final ObjectMapper mapper;

    private final CustomerUseCasePort customerUseCasePort;

    private final OutBoxRepository outBoxRepository;

    private final MessageLogRepository messageLogRepository;

    @Transactional
    public void handleReserveCustomerBalanceRequest(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
            var eventType = getEventTypeHeaderAsEnum(event.getHeaders(), "eventType");
            var requestType = getRequestTypeHeaderAsEnum(event.getHeaders(), "requestType");
            if (eventType == EventType.PAYMENT && requestType == RequestType.ACTION) {
                var placedOrderEvent = deserialize(event.getPayload());

                log.debug("Start process reserve customer balance {}", placedOrderEvent);
                var outbox = new OutBox();
                outbox.setAggregateId(placedOrderEvent.id());
                outbox.setPayload(mapper.convertValue(placedOrderEvent, JsonNode.class));
                outbox.setAggregateType(AggregateType.CUSTOMER);

                outbox.setEventType(EventType.PAYMENT);
                if (customerUseCasePort.reserveBalance(placedOrderEvent)) {
                    outbox.setResponseType(ResponseType.SUCCESS);
                } else {
                    outbox.setResponseType(ResponseType.FAILURE);
                }

                // Exported event into outbox table
                outBoxRepository.save(outbox);
                log.debug("Done process reserve customer balance {}", placedOrderEvent);
            }
            // Marked message is processed
            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    @Transactional
    public void handleCompensateCustomerBalanceRequest(Message<String> event) {
        var messageId = event.getHeaders().getId();
        if (Objects.nonNull(messageId) && !messageLogRepository.existsById(messageId)) {
            var eventType = getEventTypeHeaderAsEnum(event.getHeaders(), "eventType");
            var requestType = getRequestTypeHeaderAsEnum(event.getHeaders(), "requestType");
            if (eventType == EventType.PAYMENT && requestType == RequestType.COMPENSATE) {
                var placedOrderEvent = deserialize(event.getPayload());

                log.debug("Start process compensate customer balance {}", placedOrderEvent);
                customerUseCasePort.compensateBalance(placedOrderEvent);
                log.debug("Done process compensate customer balance {}", placedOrderEvent);
            }
            // Marked message is processed
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
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(
                    String.format("Expected record header %s not present", name));
        }
        String stringResult = new String(value, StandardCharsets.UTF_8);
        return EventType.valueOf(stringResult);
    }

    private RequestType getRequestTypeHeaderAsEnum(MessageHeaders headers, String name) {
        var value = headers.get(name, byte[].class);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(
                    String.format("Expected record header %s not present", name));
        }
        String stringResult = new String(value, StandardCharsets.UTF_8);
        return RequestType.valueOf(stringResult);
    }
}
