package net.greeta.stock.common.orchestrator;

import net.greeta.stock.common.messages.Request;
import org.reactivestreams.Publisher;

import java.util.UUID;

public interface RequestCompensator {

    Publisher<Request> compensate(UUID id);

}
