package net.greeta.stock.common.domain.workflow.orchestrator;

public interface WorkflowChain {

    void setPreviousStep(RequestCompensator previousStep);

    void setNextStep(RequestSender nextStep);

}
