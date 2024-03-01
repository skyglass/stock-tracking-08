package net.greeta.stock.order.infrastructure.message.publisher;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.publisher.EventPublisher;
import net.greeta.stock.order.common.service.OrderEventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<UUID> {

    @Override
    public Flux<UUID> publish() {
        return null; //TODO
    }

    @Override
    public void emitOrderCreated(Order dto) {
        //TODO
    }

}
