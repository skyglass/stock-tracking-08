package net.greeta.stock.inventory.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerDlqDelegate {

    private final EventHandlerDelegate eventHandlerDelegate;

    public void handleDlq(Message<String> event) {
        try {
            eventHandlerDelegate.handleDlq(event);
        } catch (Exception e) {
            if (e.getCause() != null) {
                eventHandlerDelegate.handleDlqException(event, e.getCause());
            } else {
                eventHandlerDelegate.handleDlqException(event, e);
            }
        }

    }


}
