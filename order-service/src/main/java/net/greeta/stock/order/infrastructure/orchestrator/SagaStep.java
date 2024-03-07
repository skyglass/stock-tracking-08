package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import net.greeta.stock.common.domain.dto.workflow.ResponseStatus;
import net.greeta.stock.common.domain.dto.workflow.ResponseType;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.StepName;
import net.greeta.stock.order.domain.port.OrderUseCasePort;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SagaStep {

    @Autowired
    private OrderUseCasePort orderUseCase;

    private SagaStep previousStep;

    private SagaStep nextStep;

    private OrderStatus successStatus;

    private OrderStatus failureStatus;

    protected abstract StepName getStepName();

    public void handleRequest(Order order, RequestType requestType) {
        trackRequestAction(order, getEventType(), requestType);
        orderUseCase.exportOutBoxEvent(order, getEventType(), requestType);
    }

    public void handleResponse(Order order, ResponseType responseType, ResponseStatus responseStatus) {
        if (responseType == ResponseType.ACTION) {
            if (responseStatus == ResponseStatus.SUCCESS) {
                handleSuccessResponse(order);
            } else {
                handleFailureResponse(order);
            }
        } else {
            if (responseStatus == ResponseStatus.SUCCESS) {
                handleSuccessCompensateResponse(order);
            } else {
                handleFailureCompensateResponse(order);
            }
        }
    }

    public void handleSuccessResponse(Order order) {
        trackResponseAction(order, getEventType(), ResponseType.ACTION, ResponseStatus.SUCCESS);
        if (successStatus != null) {
            setOrderStatus(order, successStatus);
        }
        if (this.nextStep != null) {
            this.nextStep.handleRequest(order, RequestType.ACTION);
        }
    }

    public void handleSuccessCompensateResponse(Order order) {
        trackResponseAction(order, getEventType(), ResponseType.COMPENSATE, ResponseStatus.SUCCESS);
        if (this.previousStep != null) {
            this.previousStep.handleRequest(order, RequestType.COMPENSATE);
        }
    }

    public void handleFailureResponse(Order order) {
        trackResponseAction(order, getEventType(), ResponseType.ACTION, ResponseStatus.FAILURE);
        if (failureStatus != null) {
            setOrderStatus(order, failureStatus);
        }
        if (this.previousStep != null) {
            this.previousStep.handleRequest(order, RequestType.COMPENSATE);
        }
    }

    public void handleFailureCompensateResponse(Order order) {
        trackResponseAction(order, getEventType(), ResponseType.COMPENSATE, ResponseStatus.FAILURE);
    }

    private void setOrderStatus(Order order, OrderStatus orderStatus) {
        orderUseCase.updateOrderStatus(order, orderStatus);
    }

    private void trackRequestAction(Order order, EventType eventType, RequestType requestType) {
        orderUseCase.trackRequestAction(order.getId(), eventType, requestType);
    }

    private void trackResponseAction(Order order, EventType eventType, ResponseType responseType, ResponseStatus responseStatus) {
        orderUseCase.trackResponseAction(order.getId(), eventType, responseType, responseStatus);
    }

    private EventType getEventType() {
        return getStepName().getEventType();
    }

    public void setPreviousStep(SagaStep previousStep) {
        this.previousStep = previousStep;
    }

    public void setNextStep(SagaStep nextStep) {
        this.nextStep = nextStep;
    }

    public void setSuccessResponseStatus(OrderStatus successStatus) {
        this.successStatus = successStatus;
    }

    public void setFailureResponseStatus(OrderStatus failureStatus) {
        this.failureStatus = failureStatus;
    }

}
