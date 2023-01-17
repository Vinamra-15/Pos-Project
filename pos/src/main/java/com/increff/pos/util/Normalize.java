package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;

import java.util.ArrayList;
import java.util.List;

public class Normalize {
    public static void normalizeForm(BrandCategoryForm form) {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
        form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
    }

    public static void normalizeForm(ProductForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()).trim());
        form.setName(StringUtil.toLowerCase(form.getName()).trim());
        form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
        form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
    }

    public static void normalizeForm(List<OrderItemForm> orderItemFormList){
        for(int i=0;i<orderItemFormList.size();i++){
            OrderItemForm orderItemForm = orderItemFormList.get(i);
            orderItemForm.setBarcode(StringUtil.toLowerCase(orderItemForm.getBarcode()).trim());
            orderItemFormList.set(i,orderItemForm);
        }
    }
}
