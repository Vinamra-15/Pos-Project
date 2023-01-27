package com.increff.pos.helper;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;

import java.util.ArrayList;
import java.util.Date;
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

    public static SalesReportForm getSalesReportForm(Date startDate, Date endDate, String brand, String category){
        SalesReportForm salesReportForm = new SalesReportForm();
        salesReportForm.setStartDate(startDate);
        salesReportForm.setEndDate(endDate);
        salesReportForm.setBrand(brand);
        salesReportForm.setCategory(category);
        return salesReportForm;
    }
    public static SignUpForm getSignUpForm(String email, String password, String confirmPassword){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail(email);
        signUpForm.setPassword(password);
        signUpForm.setConfirmPassword(confirmPassword);
        return signUpForm;
    }

    public static LoginForm getLoginForm(String email, String password){
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(email);
        loginForm.setPassword(password);
        return loginForm;
    }


    public static BrandCategoryPojo getBrandCategoryPojo(Integer id, String brand, String category) {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setId(id);
        brandCategoryPojo.setBrand(brand);
        brandCategoryPojo.setCategory(category);
        return brandCategoryPojo;
    }

    public static InventoryPojo getInventoryPojo(Integer productId, Integer quantity) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static OrderItemPojo getOrderItemPojo(Integer orderId, Integer productId, Integer quantity, Double sellingPrice) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        return orderItemPojo;
    }

    public static OrderItemForm getOrderItemForm(Integer quantity, String barcode, Double sellingPrice) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setQuantity(quantity);
        orderItemForm.setBarcode(barcode);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
    }
}