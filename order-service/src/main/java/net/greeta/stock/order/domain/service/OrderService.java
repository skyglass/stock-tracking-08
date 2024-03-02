package net.greeta.stock.order.domain.service;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderDetails;
import net.greeta.stock.common.domain.dto.order.OrderRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {

    Order placeOrder(OrderRequest request);

    OrderDetails getOrderDetails(UUID orderId);

}
