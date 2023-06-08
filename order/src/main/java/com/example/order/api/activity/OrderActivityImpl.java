package com.example.order.api.activity;

import com.example.web.api.command.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderActivityImpl implements OrderActivity{
    @Override
    public String createOrder(CreateOrderCommand command) {
        log.info("CreateOrder in Saga for Order Id : {}", command.getOrderId());
        // some task
        return "Create Order DONE";
    }
}
