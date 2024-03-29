package net.greeta.stock.inventory.domain.port;

import java.util.function.Consumer;
import org.springframework.messaging.Message;

public interface EventHandlerPort {

  Consumer<Message<String>> handleReserveProductStockRequest();

  Consumer<Message<String>> handleCompensateProductStockRequest();

  public Consumer<Message<String>> handleDlq();
}
