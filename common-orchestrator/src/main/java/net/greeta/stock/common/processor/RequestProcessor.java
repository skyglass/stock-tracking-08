package net.greeta.stock.common.processor;

import net.greeta.stock.common.messages.Request;
import net.greeta.stock.common.messages.Response;
import reactor.core.publisher.Mono;

public interface RequestProcessor<T extends Request, R extends Response> {

    Mono<R> process(T request);

}
