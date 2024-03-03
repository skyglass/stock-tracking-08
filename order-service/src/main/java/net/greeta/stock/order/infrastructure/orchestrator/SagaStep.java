package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
import net.greeta.stock.common.domain.dto.workflow.EventType;

import java.util.UUID;

public abstract class SagaStep {

    private SagaStep previousStep;

    private SagaStep nextStep;

    private OrderStatus successStatus;

    private OrderStatus failureStatus;

    private EventType requestAction;

    private EventType compensateAction;

    private EventType successAction;

    private EventType failureAction;

    protected abstract void setOrderStatus(Order order, OrderStatus orderStatus);

    protected abstract void trackAction(Order order, EventType action);

    protected abstract void onRequest(Order order);

    protected abstract void onCompensate(Order order);

    protected abstract void onSuccess(Order order);

    protected abstract void onFailure(Order order);

    public void handleRequest(Order order) {
        if (requestAction != null) {
            trackAction(order, requestAction);
            onRequest(order);
        }
    }

    public void handleCompensateRequest(Order order) {
        if (compensateAction != null) {
            trackAction(order, compensateAction);
            onCompensate(order);
        }
    }

    public void handleSuccessResponse(Order order) {
        if (successAction != null) {
            trackAction(order, successAction);
            onSuccess(order);
            if (successStatus != null) {
                setOrderStatus(order, successStatus);
            }
            if (this.nextStep != null) {
                this.nextStep.handleRequest(order);
            }
        }
    }

    public void handleFailureResponse(Order order) {
        if (failureAction != null) {
            onFailure(order);
            if (failureStatus != null) {
                setFailureStatus(failureStatus);
            }
            if (this.previousStep != null) {
                this.previousStep.handleCompensateRequest(order);
            }
        }
    }

    public void setPreviousStep(SagaStep previousStep) {
        this.previousStep = previousStep;
    }

    public void setNextStep(SagaStep nextStep) {
        this.nextStep = nextStep;
    }

    public void setSuccessStatus(OrderStatus orderStatus) {
        this.successStatus = successStatus;
    }

    public void setFailureStatus(OrderStatus orderStatus) {
        this.failureStatus = failureStatus;
    }

    public void setRequestAction(EventType requestAction) {
        this.requestAction = requestAction;
    }

    public void setCompensateAction(EventType compensateAction) {
        this.compensateAction = compensateAction;
    }

    public void setSuccessAction(EventType successAction) {
        this.successAction = successAction;
    }

    public void setFailureAction(EventType failureAction) {
        this.failureAction = failureAction;
    }

}
