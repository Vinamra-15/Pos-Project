package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.increff.pos.util.TimeUtil.getEndOfDay;
import static com.increff.pos.util.TimeUtil.getStartOfDay;

public class Validate {
    public static void validateForm(BrandCategoryForm brandCategoryForm) throws ApiException {
        if(StringUtil.isEmpty(brandCategoryForm.getBrand())||StringUtil.isEmpty(brandCategoryForm.getCategory())) {
            throw new ApiException("Field(s) cannot be empty");
        }
    }
    public static void validateForm(InventoryForm inventoryForm) throws ApiException {

        if(StringUtil.isEmpty(inventoryForm.getBarcode())|| inventoryForm.getQuantity()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }

        if(inventoryForm.getQuantity()<0)
        {
            throw new ApiException("Quantity must be a non-negative number(integer)");
        }
    }

    public static void validateForm(ProductForm productForm) throws ApiException {

        if(StringUtil.isEmpty(productForm.getName())||StringUtil.isEmpty(productForm.getBarcode())||StringUtil.isEmpty(productForm.getBrand())||StringUtil.isEmpty(productForm.getCategory())||productForm.getMrp()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }


        if(productForm.getMrp()<0)
        {
            throw new ApiException("MRP must be a non-negative number!");
        }
    }

    public static void validateForm(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemForms)
            validateForm(orderItemForm);
    }

    public static void validateForm(OrderItemForm orderItemForm) throws ApiException {
        if(StringUtil.isEmpty(orderItemForm.getBarcode())||orderItemForm.getQuantity()==null||orderItemForm.getSellingPrice()==null) {
            throw new ApiException("Field(s) cannot be empty");
        }
        if(orderItemForm.getQuantity()<0){
            throw new ApiException("Quantity must be a non-negative number!");
        }
        if(orderItemForm.getSellingPrice()<0){
            throw new ApiException("Selling Price must be a non-negative number!");
        }
    }

    public static void validate(SalesReportForm salesReportForm) {
        salesReportForm.setBrand(StringUtil.toLowerCase(salesReportForm.getBrand()));
        salesReportForm.setCategory(StringUtil.toLowerCase(salesReportForm.getCategory()));
        if(salesReportForm.getEndDate()==null) {
            salesReportForm.setEndDate(new Date());
        }
        if(salesReportForm.getStartDate()==null) {
            salesReportForm.setStartDate(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        }
        salesReportForm.setStartDate(getStartOfDay(salesReportForm.getStartDate(),Calendar.getInstance()));
        salesReportForm.setEndDate(getEndOfDay(salesReportForm.getEndDate(),Calendar.getInstance()));
    }

    public static void validateForm(SignUpForm signUpForm) throws ApiException {
        if(signUpForm.getEmail()==null||signUpForm.getPassword()==null||signUpForm.getConfirmPassword()==null){
            throw new ApiException("Field(s) cannot be empty!");
        }
        if(!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())){
            throw new ApiException("Passwords do not match!");
        }

    }
}
