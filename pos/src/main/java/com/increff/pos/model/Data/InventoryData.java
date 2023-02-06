package com.increff.pos.model.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {
    private String barcode;
    private String productName;
    private Integer quantity;
}
