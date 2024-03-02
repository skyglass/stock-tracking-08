package net.greeta.stock.customer.infrastructure.message.outbox;

import java.util.UUID;

import jakarta.persistence.*;
import net.greeta.stock.common.domain.dto.AggregateType;
import net.greeta.stock.common.domain.dto.WorkflowAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
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
  private WorkflowAction type;

  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private JsonNode payload;
}
