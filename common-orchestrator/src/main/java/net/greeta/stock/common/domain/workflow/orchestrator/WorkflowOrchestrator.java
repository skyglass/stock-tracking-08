package net.greeta.stock.common.domain.workflow.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.Response;

public interface WorkflowOrchestrator {

    Request orchestrate(Response response);

}
