package net.greeta.stock.order.common.service;


import net.greeta.stock.common.domain.dto.order.Order;

public interface OrderEventListener {

    void emitOrderCreated(Order dto);

}
