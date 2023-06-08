package com.example.web.api.controller;

import com.example.web.api.command.CreateOrderCommand;
import com.example.web.api.model.OrderRestModel;
import com.example.web.api.workflow.CreateOrderWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    private final WorkflowClient workflowClient;

    @PostMapping
    public String CreateOrder(@RequestBody OrderRestModel orderRestModel){

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setWorkflowId("custom_workflow_1")
                .setTaskQueue("queue")
                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(2).build())
                .build();

        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand
                = CreateOrderCommand.builder()
                .orderId(orderId)
                .addressId(orderRestModel.getAddressId())
                .productId(orderRestModel.getProductId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .build();
        try {
            CreateOrderWorkflow createOrderWorkflow = workflowClient.newWorkflowStub(CreateOrderWorkflow.class, workflowOptions);
            WorkflowClient.start(createOrderWorkflow::createOrderSaga, createOrderCommand);
        } catch (WorkflowExecutionAlreadyStarted e) {
            log.info("workflow already running");
        }

        return "Order Created";
    }
}
