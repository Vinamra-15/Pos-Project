package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderDetailsData;
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
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.increff.pos.util.ConvertUtil.convert;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;;

public class OrderDtoTest extends AbstractUnitTest {
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
    @Before
    public void addProduct() throws ApiException {
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
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.add(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("some product");
        productPojo2.setBarcode("!@#$234");
        productPojo2.setMrp(1400.00);
        productPojo2.setBrandId(brandCategoryPojo.getId());
//        productPojo.setBrandId(brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo2);
        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(0);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.add(inventoryPojo2);
        //inventory quantity: 0
    }
    @Test
    public void addOrderTest() throws ApiException, IOException {
        List<OrderItemForm> orderItemFormList = addSingleOrderItemToForm() ;
        OrderData orderData = orderDto.createOrder(orderItemFormList);
        assertThat(new Date().after(orderData.getDatetime()), is(true));
        assertNotNull(orderData.getId());
        assertNotNull(orderData.getInvoicePath());
    }

    @Test
    public void addEmptyFieldOrderTest() throws ApiException, IOException {
        List<OrderItemForm> orderItemFormList = addEmptyFieldOrderItemToForm() ;
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Field(s) cannot be empty");
        OrderData orderData = orderDto.createOrder(orderItemFormList);
    }

    @Test
    public void insufficientInventoryOrderTest() throws ApiException, IOException {
        List<OrderItemForm> orderItemFormList = addMultipleOrderItemToForm();
        // set quantity greater than available inventory
        OrderItemForm orderItemForm = orderItemFormList.get(1);
        orderItemForm.setQuantity(2);
        orderItemFormList.set(1,orderItemForm);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Insufficient Inventory for product with barcode: " + orderItemForm.getBarcode());
        OrderData orderData = orderDto.createOrder(orderItemFormList);
    }

    @Test
    public void getOrderDetailsTest() throws IOException, ApiException {
        List<OrderItemForm> orderItemFormList = addSingleOrderItemToForm();
        OrderData orderData = orderDto.createOrder(orderItemFormList);
        OrderDetailsData orderDetailsData = orderDto.getOrderDetails(orderData.getId());
        assertThat(new Date().after(orderDetailsData.getDatetime()), is(true));
        assertNotNull(orderDetailsData.getId());
        assertEquals(1,orderDetailsData.getItems().size());
    }

    @Test
    public void getAllOrderTest() throws IOException, ApiException {
        List<OrderItemForm> orderItemFormList = addMultipleOrderItemToForm();
        OrderData orderData = orderDto.createOrder(orderItemFormList);
        List<OrderData> orderDataList = orderDto.getAll();
        assertEquals(1,orderDataList.size());
    }

    @Test
    public void updateOrderTest() throws IOException, ApiException {
        List<OrderItemForm> orderItemFormList = addMultipleOrderItemToForm();
        OrderData orderData = orderDto.createOrder(orderItemFormList);
        orderDto.update(orderData.getId(),addSingleOrderItemToForm());
        assertEquals(1,orderDto.getOrderDetails(orderData.getId()).getItems().size());
        orderDto.update(orderData.getId(),addMultipleOrderItemToForm());
        assertEquals(2,orderDto.getOrderDetails(orderData.getId()).getItems().size());
    }

    @Test
    public void getFileResourceTest() throws IOException, ApiException {
        List<OrderItemForm> orderItemFormList = addMultipleOrderItemToForm();
        OrderData orderData = orderDto.createOrder(orderItemFormList);
        Resource resource = orderDto.getFileResource(orderData.getId());
        assertNotNull(resource);
    }





    private List<OrderItemForm> addSingleOrderItemToForm(){
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("!@#$23");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1520.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        return orderItemFormList;
    }

    private List<OrderItemForm> addEmptyFieldOrderItemToForm(){
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(null);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(null);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        return orderItemFormList;
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
