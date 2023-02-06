package com.increff.pos.model.Data;

import com.increff.pos.model.Form.OrderItemForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    private Integer id;
    private String name;  //product name

}
