package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData {
    private String brand;
    private String category;

    Integer quantity;
    Double revenue;

    public SalesReportData(String brand, String category, Integer quantity, Double revenue) {
        this.brand = brand;
        this.category = category;
        this.quantity = quantity;
        this.revenue = revenue;
    }
}
