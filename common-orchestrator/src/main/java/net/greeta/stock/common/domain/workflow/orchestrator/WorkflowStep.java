package net.greeta.stock.common.domain.workflow.orchestrator;


import net.greeta.stock.common.domain.workflow.messages.Response;

public interface WorkflowStep<T extends Response> extends
                                                        RequestSender,
                                                        RequestCompensator,
                                                        ResponseProcessor<T>,
                                                        WorkflowChain {


}
