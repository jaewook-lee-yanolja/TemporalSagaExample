package com.example.web.api.workflow;

import com.example.web.api.command.CreateOrderCommand;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreateOrderWorkflow {
    @WorkflowMethod
    String createOrderSaga(CreateOrderCommand command);
}
