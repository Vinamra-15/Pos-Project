package com.increff.pos.util;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static BrandCategoryForm getBrandCategoryForm (String brand,String category) {
        BrandCategoryForm brandCategoryForm = new BrandCategoryForm();
        brandCategoryForm.setBrand(brand);
        brandCategoryForm.setCategory(category);
        return brandCategoryForm;
    }
    public static ProductForm getProductForm (String name,String barcode,Double mrp,String brand, String category) {
        ProductForm productForm = new ProductForm();
        productForm.setName(name);
        productForm.setBarcode(barcode);
        productForm.setMrp(mrp);
        productForm.setBrand(brand);
        productForm.setCategory(category);
        return productForm;
    }

    public static InventoryForm getInventoryForm (String barcode,Integer quantity) {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return  inventoryForm;
    }

    public static List<OrderItemForm> getOrderItemArray(List<String>barcodes,
                                                        List<Integer>quantities, List<Double>sellingPrices) {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for (int i=0; i<barcodes.size(); i++) {
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setBarcode(barcodes.get(i));
            orderItemForm.setQuantity(quantities.get(i));
            orderItemForm.setSellingPrice(sellingPrices.get(i));
            orderItemFormList.add(orderItemForm);
        }
        return orderItemFormList;
    }

    public static ProductPojo getProductPojo(String name,String barcode,Double mrp,Integer brandId){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBrandId(brandId);
        return productPojo;
    }
}