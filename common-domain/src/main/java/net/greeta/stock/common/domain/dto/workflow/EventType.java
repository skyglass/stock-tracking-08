package net.greeta.stock.common.domain.dto.workflow;

public enum EventType {

    // payment
    PAYMENT_REQUEST_INITIATED(RequestType.ACTION, null, StepName.PAYMENT),
    PAYMENT_PROCESSED(null, ResponseType.SUCCESS, StepName.PAYMENT),
    PAYMENT_DECLINED(null, ResponseType.FAILURE, StepName.PAYMENT),
    PAYMENT_REFUND_INITIATED(RequestType.COMPENSATE, null, StepName.PAYMENT),

    // inventory
    INVENTORY_REQUEST_INITIATED(RequestType.ACTION, null, StepName.INVENTORY),
    INVENTORY_DEDUCTED(null, ResponseType.SUCCESS, StepName.INVENTORY),
    INVENTORY_DECLINED(null, ResponseType.FAILURE, StepName.INVENTORY),
    INVENTORY_RESTORE_INITIATED(RequestType.COMPENSATE, null, StepName.INVENTORY);

    private RequestType requestType;
    private ResponseType responseType;
    private StepName stepType;

    private EventType(RequestType requestType,
                      ResponseType responseType,
                      StepName stepType) {
        this.requestType = requestType;
        this.responseType = responseType;
        this.stepType = stepType;
    }

    public StepName getStepType() {
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
