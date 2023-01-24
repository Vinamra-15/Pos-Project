package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;

import java.util.List;

public class Normalize {
    public static void normalizePojo(BrandCategoryPojo brandCategoryPojo) {
        brandCategoryPojo.setBrand(StringUtil.toLowerCase(brandCategoryPojo.getBrand()));
        brandCategoryPojo.setCategory(StringUtil.toLowerCase(brandCategoryPojo.getCategory()));
    }

    public static void normalizePojo(ProductPojo productPojo) {
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
    }


    public static void normalizePojo(List<OrderItemForm> orderItemFormList){
        for(int i=0;i<orderItemFormList.size();i++){
            OrderItemForm orderItemForm = orderItemFormList.get(i);
            orderItemForm.setBarcode(StringUtil.toLowerCase(orderItemForm.getBarcode()).trim());
            orderItemFormList.set(i,orderItemForm);
        }
    }
}
