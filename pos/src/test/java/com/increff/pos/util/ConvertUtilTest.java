package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertUtilTest extends AbstractUnitTest {

    @Test
    public void convertBrandCategoryPojoToBrandCategoryDataTest(){
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo((Integer) 1,"allen solly","tshirts");
        BrandCategoryData brandCategoryData = ConvertUtil.convert(brandCategoryPojo);
        assertEquals("allen solly",brandCategoryData.getBrand());
        assertEquals("tshirts",brandCategoryData.getCategory());
    }
    @Test
    public void convertBrandCategoryFormToBrandCategoryPojoTest(){
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("  allen solly"," tshirts   ");
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convert(brandCategoryForm);
        assertEquals("  allen solly",brandCategoryPojo.getBrand());
        assertEquals(" tshirts   ",brandCategoryPojo.getCategory());
    }
    @Test
    public void convertToProductDataTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo(1,"allen solly","tshirts");
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        ProductData productData = ConvertUtil.convert(productPojo,brandCategoryPojo);
        assertEquals("polo tshirt",productData.getName());
        assertEquals("a1110",productData.getBarcode());
        assertEquals("tshirts",productData.getCategory());
        assertEquals("allen solly",productData.getBrand());
        assertEquals((Double) 2100.00,productData.getMrp());
    }
    @Test
    public void convertToProductPojoTest() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo(1,"allen solly","tshirts");
        ProductForm productForm = TestUtils.getProductForm("polo tshirt","a1110",2100.00,"allen solly","tshirts");
        ProductPojo productPojo = ConvertUtil.convert(productForm,brandCategoryPojo);
        assertEquals("polo tshirt",productPojo.getName());
        assertEquals("a1110",productPojo.getBarcode());
        assertEquals((Double) 2100.00,productPojo.getMrp());
        assertEquals((Integer) 1,productPojo.getBrandId());
    }

    @Test
    public void convertToInventoryDataTest(){
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        productPojo.setId(1);
        InventoryPojo inventoryPojo = TestUtils.getInventoryPojo(productPojo.getId(),100);
        InventoryData inventoryData = ConvertUtil.convert(inventoryPojo,productPojo);
        assertEquals((Integer) 100,inventoryData.getQuantity());
        assertEquals("a1110",inventoryData.getBarcode());
        assertEquals("polo tshirt",inventoryData.getProductName());
    }
    @Test
    public void convertToInventoryPojoTest() throws ApiException {
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        productPojo.setId(1);
        InventoryForm inventoryForm = TestUtils.getInventoryForm("a1110",100);
        InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo);
        assertEquals((Integer) 1,inventoryPojo.getProductId());
        assertEquals((Integer) 100,inventoryPojo.getQuantity());
    }
    @Test
    public void convertToOrderItemDataFromOrderItemPojoProductPojoTest(){
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        productPojo.setId(1);
        OrderItemPojo orderItemPojo = TestUtils.getOrderItemPojo(1,1,50,1500.00);
        OrderItemData orderItemData = ConvertUtil.convert(orderItemPojo,productPojo);
        assertEquals("a1110",orderItemData.getBarcode());
        assertEquals("polo tshirt",orderItemData.getName());
        assertEquals((Double) 1500.00,orderItemData.getSellingPrice());
        assertEquals((Integer) 50,orderItemData.getQuantity());
    }
    @Test
    public void convertToOrderItemPojoTest(){
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        productPojo.setId(1);
        OrderItemForm orderItemForm = TestUtils.getOrderItemForm(100,"a1110",1500.00);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(1);
        OrderItemPojo orderItemPojo = ConvertUtil.convert(orderItemForm,productPojo,orderPojo);
        assertEquals((Integer) 1,orderItemPojo.getOrderId());
        assertEquals((Integer) 100,orderItemPojo.getQuantity());
        assertEquals((Integer) 1,orderItemPojo.getProductId());
        assertEquals((Double) 1500.00, orderItemPojo.getSellingPrice());
    }







}
