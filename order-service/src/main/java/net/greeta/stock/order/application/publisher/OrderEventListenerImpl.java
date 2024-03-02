package net.greeta.stock.order.application.publisher;

import lombok.RequiredArgsConstructor;
import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.order.domain.service.OrderEventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener {

    @Override
    public void emitOrderCreated(Order dto) {
        //TODO
    }

}
