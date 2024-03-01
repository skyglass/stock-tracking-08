package net.greeta.stock.common.orchestrator;

public interface WorkflowChain {

    void setPreviousStep(RequestCompensator previousStep);

    void setNextStep(RequestSender nextStep);

}
