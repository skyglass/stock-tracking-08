package net.greeta.stock.order.infrastructure.orchestrator;

import net.greeta.stock.common.domain.dto.order.Order;
import net.greeta.stock.common.domain.dto.order.OrderStatus;
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

    public void handleRequest(Order order) {
            trackAction(order, requestAction);
            onRequest(order);
            orderUseCase.exportOutBoxEvent(order, requestAction);
    }

    public void handleCompensateRequest(Order order) {
        if (compensateAction != null) {
            trackAction(order, compensateAction);
            onCompensate(order);
            orderUseCase.exportOutBoxEvent(order, compensateAction);
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
            trackAction(order, failureAction);
            onFailure(order);
            if (failureStatus != null) {
                setOrderStatus(order, failureStatus);
            }
            if (this.previousStep != null) {
                this.previousStep.handleCompensateRequest(order);
            }
        }
    }

    protected void onRequest(Order order) {

    }

    protected void onCompensate(Order order) {

    }

    protected void onSuccess(Order order) {

    }

    protected void onFailure(Order order) {

    }

    private void setOrderStatus(Order order, OrderStatus orderStatus) {
        orderUseCase.updateOrderStatus(order, orderStatus);
    }

    private void trackAction(Order order, EventType action) {
        orderUseCase.trackAction(order.getId(), action);
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
