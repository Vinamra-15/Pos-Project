package com.increff.pos.util;

import com.increff.pos.model.Form.*;
import com.increff.pos.service.ApiException;

import java.util.List;

public class Validate {
    private static final String EMAIL_PATTERN = "[a-z\\d]+@[a-z]+\\.[a-z]{2,3}";

    private static boolean isValidEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }
    private static boolean isValidBarcode(String barcode) {
        return barcode.trim().matches("\\w+");
    }
    public static void validateForm(BrandCategoryForm brandCategoryForm) throws ApiException {
        if(StringUtil.isEmpty(brandCategoryForm.getBrand())) {
            throw new ApiException("Brand field cannot be empty");
        }
        if(StringUtil.isEmpty(brandCategoryForm.getCategory())) {
            throw new ApiException("Category field cannot be empty");
        }
    }
    public static void validateForm(InventoryForm inventoryForm) throws ApiException {

        if(StringUtil.isEmpty(inventoryForm.getBarcode())) {
            throw new ApiException("Barcode field cannot be empty!");
        }
        if(!isValidBarcode(inventoryForm.getBarcode())){
            throw new ApiException("Please enter alphanumeric barcode!");
        }
        if(inventoryForm.getQuantity()==null) {
            throw new ApiException("Quantity field cannot be empty!");
        }

        if(inventoryForm.getQuantity()<0)
        {
            throw new ApiException("Quantity must be a non-negative number(integer)");
        }
    }

    public static void validateForm(ProductForm productForm) throws ApiException {

        if(StringUtil.isEmpty(productForm.getBarcode())) {
            throw new ApiException("Barcode field cannot be empty!");
        }
        if(!isValidBarcode(productForm.getBarcode())){
            throw new ApiException("Please enter alphanumeric barcode!");
        }
        if(StringUtil.isEmpty(productForm.getName())) {
            throw new ApiException("Name field cannot be empty!");
        }
        if(StringUtil.isEmpty(productForm.getBrand())) {
            throw new ApiException("Brand Field cannot be empty!");
        }
        if(StringUtil.isEmpty(productForm.getCategory())) {
            throw new ApiException("Category field cannot be empty");
        }
        if(productForm.getMrp()==null) {
            throw new ApiException("MRP field cannot be empty");
        }
        if(productForm.getMrp()<0)
        {
            throw new ApiException("MRP must be a non-negative number!");
        }
    }

    public static void validateForm(List<OrderItemForm> orderItemForms) throws ApiException {
        if(orderItemForms==null||orderItemForms.isEmpty()){
            throw new ApiException("Order Items cannot be empty!");
        }
        for(OrderItemForm orderItemForm:orderItemForms)
            validateForm(orderItemForm);
    }

    public static void validateForm(OrderItemForm orderItemForm) throws ApiException {
        if(StringUtil.isEmpty(orderItemForm.getBarcode())) {
            throw new ApiException("Barcode field cannot be empty!");
        }
        if(!isValidBarcode(orderItemForm.getBarcode())){
            throw new ApiException("Please enter alphanumeric barcode!");
        }
        if(orderItemForm.getQuantity()==null) {
            throw new ApiException("Quantity field cannot be empty!");
        }
        if(orderItemForm.getSellingPrice()==null) {
            throw new ApiException("Selling Price field cannot be empty!");
        }
        if(orderItemForm.getQuantity()<0){
            throw new ApiException("Quantity must be a non-negative number!");
        }
        if(orderItemForm.getSellingPrice()<0){
            throw new ApiException("Selling Price must be a non-negative number!");
        }
    }

    public static void validateForm(SignUpForm signUpForm) throws ApiException {

        if (StringUtil.isEmpty(signUpForm.getEmail())) {
            throw new ApiException("Please enter email!");
        }
        if (!isValidEmail(signUpForm.getEmail())) {
            throw new ApiException("Invalid email!");
        }
        if (StringUtil.isEmpty(signUpForm.getPassword())) {
            throw new ApiException("Please enter password!");
        }
        if (StringUtil.isEmpty(signUpForm.getConfirmPassword())) {
            throw new ApiException("Please confirm password!");
        }
        if (!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())) {
            throw new ApiException("Passwords do not match!");
        }
    }
}
