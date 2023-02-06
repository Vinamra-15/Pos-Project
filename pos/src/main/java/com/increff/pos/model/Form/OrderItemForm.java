package com.increff.pos.model.Form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {

    private String barcode; //product barcode
    private Integer quantity;
    private Double sellingPrice;

}
