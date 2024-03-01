package net.greeta.stock.common.domain.dto.order;

public enum WorkflowAction {

    // payment
    PAYMENT_REQUEST_INITIATED,
    PAYMENT_PROCESSED,
    PAYMENT_DECLINED,
    PAYMENT_REFUND_INITIATED,

    // inventory
    INVENTORY_REQUEST_INITIATED,
    INVENTORY_DEDUCTED,
    INVENTORY_DECLINED,
    INVENTORY_RESTORE_INITIATED

}
