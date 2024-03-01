package net.greeta.stock.common.orchestrator;

import net.greeta.stock.common.messages.Response;

public interface WorkflowStep<T extends Response> extends
                                                        RequestSender,
                                                        RequestCompensator,
                                                        ResponseProcessor<T>,
                                                        WorkflowChain {


}
