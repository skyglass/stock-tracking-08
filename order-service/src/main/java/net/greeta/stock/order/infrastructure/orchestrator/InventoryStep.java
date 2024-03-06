package net.greeta.stock.order.infrastructure.orchestrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.greeta.stock.common.domain.dto.workflow.orchestrator.StepName;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryStep extends SagaStep {


    @Override
    protected StepName getStepName() {
        return StepName.INVENTORY;
    }
}
