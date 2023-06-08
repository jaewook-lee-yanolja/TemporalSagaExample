package com.example.web.api.event;

import com.example.web.api.command.CreateOrderCommand;
import com.example.web.api.workflow.CreateOrderWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CreateOrderWorkflowListener {
    private final WorkflowClient workflowClient;
    @EventListener
    public void onApplicationEvent(CreateOrderCommand createOrderCommand) {
        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setWorkflowId("custom_workflow_1")
                .setTaskQueue("queue")
                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(2).build())
                .build();
        try {
            CreateOrderWorkflow createOrderWorkflow = workflowClient.newWorkflowStub(CreateOrderWorkflow.class, workflowOptions);
            WorkflowClient.start(createOrderWorkflow::createOrderSaga, createOrderCommand);
        } catch (WorkflowExecutionAlreadyStarted e) {
            log.info("workflow already running");
        }
    }
}
