package net.greeta.stock.common.domain.workflow.orchestrator;

import net.greeta.stock.common.domain.workflow.messages.Request;

import java.util.UUID;

public interface RequestSender {

    Request send(UUID id);

}
