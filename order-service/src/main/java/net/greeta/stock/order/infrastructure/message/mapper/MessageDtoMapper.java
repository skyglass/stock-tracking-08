package net.greeta.stock.order.infrastructure.message.mapper;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.messages.inventory.InventoryRequest;
import net.greeta.stock.common.messages.payment.PaymentRequest;

import java.util.UUID;

public class MessageDtoMapper {

    public static PaymentRequest toPaymentProcessRequest(Order dto) {
        return PaymentRequest.Process.builder()
                                     .orderId(dto.getId())
                                     .amount(dto.getQuantity())
                                     .customerId(dto.getCustomerId())
                                     .build();
    }

    public static PaymentRequest toPaymentRefundRequest(UUID orderId) {
        return PaymentRequest.Refund.builder()
                                    .orderId(orderId)
                                    .build();
    }

    public static InventoryRequest toInventoryDeductRequest(Order dto) {
        return InventoryRequest.Deduct.builder()
                                      .orderId(dto.getId())
                                      .productId(dto.getProductId())
                                      .quantity(dto.getQuantity())
                                      .build();
    }

    public static InventoryRequest toInventoryRestoreRequest(UUID orderId) {
        return InventoryRequest.Restore.builder()
                                       .orderId(orderId)
                                       .build();
    }

}
