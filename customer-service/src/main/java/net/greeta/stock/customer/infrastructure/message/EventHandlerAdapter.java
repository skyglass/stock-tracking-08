package net.greeta.stock.customer.infrastructure.message;

import java.util.function.Consumer;

import net.greeta.stock.customer.domain.port.EventHandlerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerAdapter implements EventHandlerPort {

  private final EventHandlerDelegate eventHandlerDelegate;

  @Bean
  @Override
  public Consumer<Message<String>> handleReserveCustomerBalanceRequest() {
    return eventHandlerDelegate::handleReserveCustomerBalanceRequest;
  }

  @Bean
  @Override
  public Consumer<Message<String>> handleCompensateCustomerBalanceRequest() {
    return eventHandlerDelegate::handleCompensateCustomerBalanceRequest;
  }

}
