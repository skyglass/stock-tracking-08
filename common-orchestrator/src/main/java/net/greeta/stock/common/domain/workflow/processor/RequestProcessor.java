package net.greeta.stock.common.domain.workflow.processor;

import net.greeta.stock.common.domain.workflow.messages.Request;
import net.greeta.stock.common.domain.workflow.messages.Response;

public interface RequestProcessor<T extends Request, R extends Response> {

    R process(T request);

}
