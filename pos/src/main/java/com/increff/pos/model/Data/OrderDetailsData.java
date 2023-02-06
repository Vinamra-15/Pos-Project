package com.increff.pos.model.Data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDetailsData {
    private Integer id;
    private Date datetime;
    private List<OrderItemData> items;
}
