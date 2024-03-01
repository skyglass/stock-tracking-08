package net.greeta.stock.common.orchestrator;

import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.Response;
import org.reactivestreams.Publisher;

public interface ResponseProcessor<T extends Response> {

    Publisher<Request> process(T response);

}
