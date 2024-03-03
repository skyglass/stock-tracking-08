package net.greeta.stock.common.domain.dto.workflow;

public enum EventType {

    // payment
    PAYMENT_REQUEST_INITIATED(RequestType.ACTION, null, StepType.PAYMENT),
    PAYMENT_PROCESSED(null, ResponseType.SUCCESS, StepType.PAYMENT),
    PAYMENT_DECLINED(null, ResponseType.FAILURE, StepType.PAYMENT),
    PAYMENT_REFUND_INITIATED(RequestType.COMPENSATE, null, StepType.PAYMENT),

    // inventory
    INVENTORY_REQUEST_INITIATED(RequestType.ACTION, null, StepType.INVENTORY),
    INVENTORY_DEDUCTED(null, ResponseType.SUCCESS, StepType.INVENTORY),
    INVENTORY_DECLINED(null, ResponseType.FAILURE, StepType.INVENTORY),
    INVENTORY_RESTORE_INITIATED(RequestType.COMPENSATE, null, StepType.INVENTORY);

    private RequestType requestType;
    private ResponseType responseType;
    private StepType stepType;

    private EventType(RequestType requestType,
                      ResponseType responseType,
                      StepType stepType) {
        this.requestType = requestType;
        this.responseType = responseType;
        this.stepType = stepType;
    }

    public StepType getStepType() {
        return stepType;
    }

    public boolean isRequest() {
        return requestType == RequestType.ACTION;
    }

    public boolean isCompensateRequest() {
        return requestType == RequestType.COMPENSATE;
    }

    public boolean isSuccessResponse() {
        return responseType == ResponseType.SUCCESS;
    }

    public boolean isFailureResponse() {
        return responseType == ResponseType.FAILURE;
    }

}
