package net.greeta.stock.order.domain.service;

import net.greeta.stock.common.domain.dto.order.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderFulfillmentService {

    Order get(UUID orderId);

    Order complete(UUID orderId);

    Order cancel(UUID orderId);

}
