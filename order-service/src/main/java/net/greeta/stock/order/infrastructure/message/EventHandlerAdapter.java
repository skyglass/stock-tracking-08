package net.greeta.stock.order.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.order.domain.port.EventHandlerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerAdapter implements EventHandlerPort {

  private final EventHandlerDelegate eventHandlerDelegate;

  @Bean
  @Override
  public Consumer<Message<String>> reserveCustomerBalanceStage() {
    return eventHandlerDelegate::reserveCustomerBalanceStage;
  }

  @Bean
  @Override
  public Consumer<Message<String>> reserveProductStockStage() {
    return eventHandlerDelegate::reserveProductStockStage;
  }

}
