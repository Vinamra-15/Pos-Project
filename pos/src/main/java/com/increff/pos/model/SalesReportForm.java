package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SalesReportForm {
    Date startDate;
    Date endDate;
    String brand;
    String category;
}
