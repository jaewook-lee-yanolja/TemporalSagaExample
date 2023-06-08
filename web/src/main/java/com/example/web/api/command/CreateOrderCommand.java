package com.example.web.api.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderCommand {
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

}
