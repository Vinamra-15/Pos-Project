package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderItemDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderItemDto orderItemDto;

    private Integer orderId;
    private Integer product1Id;

    @Before
    public void addOrderProductBrandCategory() throws ApiException, IOException {
            BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
            brandCategoryPojo.setBrand("some brand");
            brandCategoryPojo.setCategory("some category");
            brandCategoryService.addBrandCategory(brandCategoryPojo);

            ProductPojo productPojo = new ProductPojo();
            productPojo.setName("some product");
            productPojo.setBarcode("!@#$23");
            productPojo.setMrp(1200.00);
            productPojo.setBrandId(brandCategoryPojo.getId());
//        productPojo.setBrandId(brandCategoryService.getByBrandCategory("some brand","some category").getId());
            productService.addProduct(productPojo);
            product1Id = productPojo.getId();
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setQuantity(20);
            inventoryPojo.setProductId(productPojo.getId());
            inventoryService.addInventory(inventoryPojo);

            ProductPojo productPojo2 = new ProductPojo();
            productPojo2.setName("some product");
            productPojo2.setBarcode("!@#$234");
            productPojo2.setMrp(1400.00);
            productPojo2.setBrandId(brandCategoryPojo.getId());
//        productPojo.setBrandId(brandCategoryService.getByBrandCategory("some brand","some category").getId());
            productService.addProduct(productPojo2);
            InventoryPojo inventoryPojo2 = new InventoryPojo();
            inventoryPojo2.setQuantity(0);
            inventoryPojo2.setProductId(productPojo2.getId());
            inventoryService.addInventory(inventoryPojo2);
            //inventory quantity: 0

        OrderData orderData = orderDto.createOrder(addMultipleOrderItemToForm());
        orderId = orderData.getId();
    }

    @Test
    public void getOrderItemsTest() throws ApiException {
        List<OrderItemData> orderItemDataList = orderItemDto.get(orderId);
        assertEquals(2,orderItemDataList.size());
    }

    @Test
    public void getInvalidOrderItemsTest() throws ApiException {// get Order Items for invalid OrderId
        List<OrderItemData> orderItemDataList = orderItemDto.get(86453);
        assertEquals(0,orderItemDataList.size());
    }

    @Test
    public void getByOrderIdProductIdTest() throws ApiException {
        OrderItemData orderItemData = orderItemDto.getByOrderIdProductId(orderId,product1Id);
        assertEquals("some product",orderItemData.getName());
        assertEquals("!@#$23",orderItemData.getBarcode());
        assertEquals((Integer) 6,orderItemData.getQuantity());
        assertEquals((Double) 1520.0,orderItemData.getSellingPrice());
    }

    @Test
    public void getByInvalidOrderIdProductIdTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        OrderItemData orderItemData = orderItemDto.getByOrderIdProductId(52431,product1Id);
        assertEquals("some product",orderItemData.getName());
        assertEquals("!@#$23",orderItemData.getBarcode());
        assertEquals((Integer) 6,orderItemData.getQuantity());
        assertEquals((Double) 1520.0,orderItemData.getSellingPrice());
    }

    private List<OrderItemForm> addMultipleOrderItemToForm(){
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("!@#$23");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1520.0);

        barcodes.add("!@#$234");
        quantities.add(0);
        sellingPrices.add(1520.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        return orderItemFormList;
    }

}
