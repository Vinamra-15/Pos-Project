package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Test;

import java.util.*;

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

    @Test
    public void convertToOrderData(){
        OrderPojo orderPojo = TestUtils.getOrderPojo();
        OrderData orderData = ConvertUtil.convert(orderPojo);
        assertEquals(new Date(10000101),orderData.getDatetime());
        assertEquals("billPath",orderData.getInvoicePath());
    }

    @Test
    public void getOrderItemDetailsListTest(){
        ProductPojo productPojo = TestUtils.getProductPojo("polo tshirt","a1110",2100.00,1);
        productPojo.setId(1);
        OrderItemPojo orderItemPojo = TestUtils.getOrderItemPojo(1,1,1,1200.00);

        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(orderItemPojo);
        List<ProductPojo> productPojoList = new ArrayList<>();
        productPojoList.add(productPojo);
        List<OrderItemData> orderItemDataList = ConvertUtil.getOrderItemDetailsList(orderItemPojoList,productPojoList);
        assertEquals(1,orderItemDataList.size());
        assertEquals("a1110",orderItemDataList.get(0).getBarcode());
        assertEquals((Integer) 1,orderItemDataList.get(0).getQuantity());
    }

    @Test
    public void convertToDaySalesDataListTest(){
        DaySalesPojo daySalesPojo = TestUtils.getDaySalesPojo(new Date(100000),12000.00,2,5);
        List<DaySalesPojo> daySalesPojoList = new ArrayList<>();
        daySalesPojoList.add(daySalesPojo);
        List<DaySalesData> daySalesDataList = ConvertUtil.convert(daySalesPojoList);
        assertEquals(1,daySalesDataList.size());
        assertEquals(new Date(100000),daySalesDataList.get(0).getDate());
        assertEquals((Double) 12000.00,daySalesDataList.get(0).getTotal_revenue());
        assertEquals((Integer) 2,daySalesDataList.get(0).getInvoiced_orders_count());
        assertEquals((Integer) 5,daySalesDataList.get(0).getInvoiced_items_count());
    }
    @Test
    public void convertMapToSalesReportDataListTest(){
        SalesReportData salesReportData = new SalesReportData("brand","category",1,1500.00);
        Map<Integer,SalesReportData> brandSalesMapping = new HashMap<>();
        brandSalesMapping.put(1,salesReportData);
        List<SalesReportData> salesReportDataList = ConvertUtil.convertMapToSalesReportDataList(brandSalesMapping);
        assertEquals(1,salesReportDataList.size());
        assertEquals("brand",salesReportDataList.get(0).getBrand());
        assertEquals("category",salesReportDataList.get(0).getCategory());
        assertEquals((Integer) 1,salesReportDataList.get(0).getQuantity());
        assertEquals((Double) 1500.00,salesReportDataList.get(0).getRevenue());
    }

    @Test
    public void convertMaptoInventoryReportDataListTest(){
        InventoryReportData inventoryReportData = new InventoryReportData("brand","category",2);
        Map<Integer,InventoryReportData> brandIdToInventoryReportDataMap = new HashMap<>();
        brandIdToInventoryReportDataMap.put(1,inventoryReportData);
        List<InventoryReportData> inventoryReportDataList = ConvertUtil.convertMaptoInventoryReportDataList(brandIdToInventoryReportDataMap);
        assertEquals(1,inventoryReportDataList.size());
        assertEquals("brand",inventoryReportDataList.get(0).getBrand());
        assertEquals("category",inventoryReportDataList.get(0).getCategory());
        assertEquals((Integer) 2,inventoryReportDataList.get(0).getQuantity());
    }
    @Test
    public void convertSignUpFormToUserPojoTest(){
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyz@increff.com","Pass1234","Pass1234");
        UserPojo userPojo = ConvertUtil.convert(signUpForm);
        assertEquals("xyz@increff.com",userPojo.getEmail());
        assertEquals("Pass1234",userPojo.getPassword());
    }
    @Test
    public void convertUserPojoToUserDataTest(){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail("xyz@increff.com");
        userPojo.setPassword("Pass1234");
        UserData userData = ConvertUtil.convert(userPojo);
        assertEquals("xyz@increff.com",userData.getEmail());
    }
    @Test
    public void convertUserFormToUserPojoTest(){
        UserForm userForm = new UserForm();
        userForm.setEmail("xyz@increff.com");
        userForm.setPassword("Pass1234");

        UserPojo userPojo = ConvertUtil.convert(userForm);
        assertEquals("xyz@increff.com",userPojo.getEmail());
        assertEquals("Pass1234",userPojo.getPassword());
    }

}
