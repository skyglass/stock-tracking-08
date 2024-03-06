package net.greeta.stock.order.infrastructure.message.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.greeta.stock.common.domain.dto.workflow.AggregateType;
import net.greeta.stock.common.domain.dto.workflow.EventType;
import net.greeta.stock.common.domain.dto.workflow.RequestType;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutBox {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AggregateType aggregateType;

  @Column(nullable = false)
  private UUID aggregateId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EventType eventType;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RequestType requestType;

  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private JsonNode payload;
}
