package net.greeta.stock.common.domain.workflow.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.Response;

public interface ResponseProcessor<T extends Response> {

    Request process(T response);

}
