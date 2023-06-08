package com.example.web.api.workflow;

import com.example.order.api.activity.OrderActivity;
import com.example.web.api.command.CreateOrderCommand;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class CreateOrderWorkflowImpl implements CreateOrderWorkflow{

    private OrderActivity orderActivity;
    @Override
    public String createOrderSaga(CreateOrderCommand createOrderCommand) {
        init();

        try {
            orderActivity.createOrder(createOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
        }
        return "Workflow DONE";
    }

    public void init(){
        RetryOptions retryOptions = RetryOptions.newBuilder()
                .setInitialInterval(Duration.ofSeconds(1))
                .setMaximumInterval(Duration.ofSeconds(5000))
                .setBackoffCoefficient(1).setMaximumAttempts(5)
                .build();

        ActivityOptions options = ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(60))
                .setRetryOptions(retryOptions)
                .build();

        orderActivity = Workflow.newActivityStub(OrderActivity.class, options);
    }
}
