package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
public class DaySalesData {
    private Date date;

    private Integer invoiced_orders_count;

    private Integer invoiced_items_count;

    private Double total_revenue;

}
