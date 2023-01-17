package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pos_day_sales")
public class DaySalesPojo {
    @Id
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private Integer invoiced_orders_count;
    @Column(nullable = false)
    private Integer invoiced_items_count;
    @Column(nullable = false)
    private Double total_revenue;

}
