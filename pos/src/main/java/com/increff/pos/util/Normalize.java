package com.increff.pos.util;

import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;

public class Normalize {
    public static void normalizePojo(BrandCategoryPojo brandCategoryPojo) {
        brandCategoryPojo.setBrand(StringUtil.toLowerCase(brandCategoryPojo.getBrand()));
        brandCategoryPojo.setCategory(StringUtil.toLowerCase(brandCategoryPojo.getCategory()));
    }

    public static void normalizePojo(ProductPojo productPojo) {
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
        productPojo.setMrp((normalizeDouble(productPojo.getMrp())));
    }

    public static void normalizePojo(OrderItemPojo orderItemPojo){
        orderItemPojo.setSellingPrice(normalizeDouble(orderItemPojo.getSellingPrice()));
    }

    private static Double normalizeDouble(Double input) {
        String[] parts = input.toString().split("\\.");
        if (parts.length == 1) return input;

        String integerPart = parts[0];
        String decimalPart = parts[1];

        if (decimalPart.length() <= 2) return input;
        decimalPart = decimalPart.substring(0, 2);

        String doubleStr = integerPart + "." + decimalPart;
        return Double.parseDouble(doubleStr);
    }
}
