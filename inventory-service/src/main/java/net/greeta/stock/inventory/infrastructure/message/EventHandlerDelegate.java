package net.greeta.stock.inventory.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.inventory.domain.PlacedOrderEvent;
import net.greeta.stock.inventory.domain.port.ProductUseCasePort;
import net.greeta.stock.inventory.infrastructure.message.log.MessageLog;
import net.greeta.stock.inventory.infrastructure.message.log.MessageLogRepository;
import net.greeta.stock.inventory.infrastructure.message.outbox.OutBox;
import net.greeta.stock.inventory.infrastructure.message.outbox.OutBoxRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlerDelegate {

    private final ObjectMapper mapper;

    private final ProductUseCasePort productUseCase;

    private final MessageLogRepository messageLogRepository;

    private final OutBoxRepository outBoxRepository;

    @Transactional
    public void handleReserveProductStockRequest(Message<String> event) {
        var messageId = event.getHeaders().getId();
        log.debug("EventHandlerAdapter.handleReserveProductStockRequest: Started processing message {}", messageId);
        if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
            var eventType = getHeaderAsEnum(event.getHeaders(), "eventType");
            if (eventType == EventType.INVENTORY_REQUEST_INITIATED) {
                var placedOrderEvent = deserialize(event.getPayload());

                log.debug("Start process reserve product stock {}", placedOrderEvent);
                var outbox = new OutBox();
                outbox.setAggregateId(placedOrderEvent.id());
                outbox.setAggregateType(AggregateType.PRODUCT);
                outbox.setPayload(mapper.convertValue(placedOrderEvent, JsonNode.class));

                if (productUseCase.reserveProduct(placedOrderEvent)) {
                    outbox.setType(EventType.INVENTORY_DEDUCTED);
                } else {
                    outbox.setType(EventType.INVENTORY_DECLINED);
                }

                outBoxRepository.save(outbox);
                log.debug("Done process reserve product stock {}", placedOrderEvent);
            }

            messageLogRepository.save(new MessageLog(messageId, Timestamp.from(Instant.now())));
        }
    }

    @Transactional
    public void handleDlq(Message<String> event) {
        var messageId = event.getHeaders().getId();
        log.debug("EventHandlerAdapter.handleReserveProductStockRequest: Started processing message {}", messageId);
        if (Objects.nonNull(messageId) && !messageLogRepository.isMessageProcessed(messageId)) {
            var placedOrderEvent = deserialize(event.getPayload());

            log.debug("Start failed process reserve product stock event {}", placedOrderEvent);
            var outbox = new OutBox();
            outbox.setAggregateId(placedOrderEvent.id());
            outbox.setAggregateType(AggregateType.PRODUCT);
            outbox.setPayload(mapper.convertValue(placedOrderEvent, JsonNode.class));
            outbox.setType(EventType.INVENTORY_DECLINED);

            outBoxRepository.save(outbox);
            log.debug("Done failed process reserve product stock {}", placedOrderEvent);

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
