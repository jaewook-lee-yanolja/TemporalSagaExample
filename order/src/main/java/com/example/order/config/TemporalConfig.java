package com.example.order.config;

import com.example.order.api.activity.OrderActivity;
import com.example.web.api.workflow.CreateOrderWorkflow;
import com.example.web.api.workflow.CreateOrderWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkflowImplementationOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {
    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions
                .newBuilder()
                .setTarget("127.0.0.1:7233")
                .build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs){
        return WorkflowClient.newInstance(workflowServiceStubs,
                WorkflowClientOptions.newBuilder()
                        .setNamespace("default")
                        .build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient, OrderActivity orderActivity) throws ClassNotFoundException {
        WorkerFactory workerFactory = WorkerFactory.newInstance(workflowClient);
        WorkflowImplementationOptions workflowImplementationOptions =
                WorkflowImplementationOptions.newBuilder()
                        .setFailWorkflowExceptionTypes(NullPointerException.class)
                        .build();

        //create Worker
        Worker worker = workerFactory.newWorker("queue");

        //register workflows with worker
        worker.registerActivitiesImplementations(workflowImplementationOptions, CreateOrderWorkflowImpl.class);

        //register activities with worker
        worker.registerActivitiesImplementations(orderActivity);

        workerFactory.start();
        return workerFactory;
    }
}

