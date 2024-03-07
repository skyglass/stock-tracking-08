package net.greeta.stock.common.domain.dto.workflow;

public enum ResponseType {

    ACTION,
    COMPENSATE;

    public static ResponseType getResponseType(RequestType requestType) {
        return requestType == RequestType.ACTION ? ResponseType.ACTION : ResponseType.COMPENSATE;
    }
}
