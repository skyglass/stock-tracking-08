package net.greeta.stock.common.domain.workflow.exception;

public class EventAlreadyProcessedException extends RuntimeException {

    private static final String MESSAGE = "The event is already processed";

    public EventAlreadyProcessedException() {
        super(MESSAGE);
    }
}
