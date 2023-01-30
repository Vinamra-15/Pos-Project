package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

public class ValidateTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void validateNullInBrandCategoryFormTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("", " category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand field cannot be empty");
        Validate.validateForm(brandCategoryForm);
    }

    @Test
    public void validateNullCategoryInBrandCategoryFormTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" brand", "");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Category field cannot be empty");
        Validate.validateForm(brandCategoryForm);
    }

    @Test
    public void validateNullBarcodeInInventoryFormTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("", 1);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode field cannot be empty!");
        Validate.validateForm(inventoryForm);
    }

    @Test
    public void validateNullQuantityInInventoryFormTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("a1122", null);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity field cannot be empty!");
        Validate.validateForm(inventoryForm);
    }

    @Test
    public void validateNegativeQuantityInInventoryFormTest() throws ApiException {
        InventoryForm inventoryForm = TestUtils.getInventoryForm("a1122", -200);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity must be a non-negative number(integer)");
        Validate.validateForm(inventoryForm);
    }

    @Test
    public void validateNullBarcodeInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("name", "",1200.00,"brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode field cannot be empty!");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNullNameInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("", "afda",1200.00,"brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Name field cannot be empty!");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNullMrpInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("name", "adsf",null,"brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("MRP field cannot be empty");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNullBrandInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("name", "adsf",1502.0,"","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Brand Field cannot be empty!");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNullCategoryInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("name", "adsf",1502.0,"brand","");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Category field cannot be empty");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNegativeMrpInProductFormTest() throws ApiException {
        ProductForm productForm = TestUtils.getProductForm("name", "adsf",-100.0,"brand","category");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("MRP must be a non-negative number!");
        Validate.validateForm(productForm);
    }

    @Test
    public void validateNullBarcodeInOrderItemFormTest() throws ApiException {
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(1,"",1500.0);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Barcode field cannot be empty!");
        Validate.validateForm(orderItemForm);
    }

    @Test
    public void validateNullQuantityInOrderItemFormTest() throws ApiException {
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(null,"asd",1500.0);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity field cannot be empty!");
        Validate.validateForm(orderItemForm);
    }

    @Test
    public void validateNullSellingPriceInOrderItemFormTest() throws ApiException {
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(2,"asd",null);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling Price field cannot be empty!");
        Validate.validateForm(orderItemForm);
    }

    @Test
    public void validateNegativeQuantityInOrderItemFormTest() throws ApiException {
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(-2,"asd",1500.00);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Quantity must be a non-negative number!");
        Validate.validateForm(orderItemForm);
    }

    @Test
    public void validateNegativeSellingPriceInOrderItemFormTest() throws ApiException {
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(2,"asd",-500.00);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Selling Price must be a non-negative number!");
        Validate.validateForm(orderItemForm);
    }

    @Test
    public void validateOrderItemFormListTest() throws ApiException {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Order Items cannot be empty!");
        Validate.validateForm(orderItemFormList);
    }

    @Test
    public void validateNullEmailSignUpFormTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("","pass1234","pass1234");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please enter email!");
        Validate.validateForm(signUpForm);
    }
    @Test
    public void validateInvalidEmailSignUpFormTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("   afads$.com","pass1234","pass1234");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Invalid email!");
        Validate.validateForm(signUpForm);
    }

    @Test
    public void validateNullPasswordSignUpFormTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyz@increff.com","","pass1234");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please enter password!");
        Validate.validateForm(signUpForm);
    }
    @Test
    public void validateNullConfirmPasswordSignUpFormTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyz@increff.com","pass1234","");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Please confirm password!");
        Validate.validateForm(signUpForm);
    }

    @Test
    public void validatePasswordNotEqualToConfirmPasswordSignUpFormTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyz@increff.com","pass1234","asdf");
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Passwords do not match!");
        Validate.validateForm(signUpForm);
    }









}
