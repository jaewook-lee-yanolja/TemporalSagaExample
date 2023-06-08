package com.example.order.api.activity;

import com.example.web.api.command.CreateOrderCommand;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OrderActivity {
    @ActivityMethod
    String createOrder(CreateOrderCommand command);
}
