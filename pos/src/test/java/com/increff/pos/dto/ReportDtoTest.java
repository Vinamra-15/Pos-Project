package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.helper.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportDtoTest extends AbstractUnitTest {
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
    private ReportDto reportDto;

    @Autowired
    private ReportService reportService;



    @Before
    public void addOrder() throws ApiException, IOException {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setBrand("some brand");
        brandCategoryPojo.setCategory("some category");
        brandCategoryService.addBrandCategory(brandCategoryPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName("some product");
        productPojo.setBarcode("abc123");
        productPojo.setMrp(2400.00);
        productPojo.setBrandId(brandCategoryPojo.getId());
        productService.addProduct(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.addInventory(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("some product");
        productPojo2.setBarcode("abc1234");
        productPojo2.setMrp(2600.00);
        productPojo2.setBrandId(brandCategoryPojo.getId());
//        productPojo.setBrandId(brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo2);
        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(2);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.addInventory(inventoryPojo2);
        //inventory quantity: 0

        List<OrderItemForm> orderItemFormList = addMultipleOrderItemToForm();
        orderDto.createOrder(orderItemFormList);
    }


    @Test
    public void getSalesReportTest() throws ApiException {
        SalesReportForm salesReportForm = TestUtils.getSalesReportForm(null,null, "some brand","some category");
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(salesReportForm);
        assertEquals("some brand",salesReportDataList.get(0).getBrand());
        assertEquals("some category",salesReportDataList.get(0).getCategory());
        assertEquals((Integer) 6,salesReportDataList.get(0).getQuantity());

        SalesReportForm salesReportForm2 = TestUtils.getSalesReportForm(null,null, "","some category");
        List<SalesReportData> salesReportDataList2 = reportDto.getSalesReport(salesReportForm2);
        assertEquals("some brand",salesReportDataList.get(0).getBrand());
        assertEquals("some category",salesReportDataList.get(0).getCategory());
        assertEquals((Integer) 6,salesReportDataList.get(0).getQuantity());

        SalesReportForm salesReportForm3 = TestUtils.getSalesReportForm(null,null, "some brand","");
        List<SalesReportData> salesReportDataList3 = reportDto.getSalesReport(salesReportForm3);
        assertEquals("some brand",salesReportDataList.get(0).getBrand());
        assertEquals("some category",salesReportDataList.get(0).getCategory());
        assertEquals((Integer) 6,salesReportDataList.get(0).getQuantity());

        SalesReportForm salesReportForm4 = TestUtils.getSalesReportForm(null,null, "","");
        List<SalesReportData> salesReportDataList4 = reportDto.getSalesReport(salesReportForm4);
        assertEquals("some brand",salesReportDataList.get(0).getBrand());
        assertEquals("some category",salesReportDataList.get(0).getCategory());
        assertEquals((Integer) 6,salesReportDataList.get(0).getQuantity());

    }
    @Test
    public void getInventoryReportTest() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = reportDto.getInventoryReport();
        Integer size = inventoryReportDataList.size();
        assertEquals("some brand",inventoryReportDataList.get(size-1).getBrand());
        assertEquals("some category",inventoryReportDataList.get(size-1).getCategory());
        assertEquals((Integer) 16,inventoryReportDataList.get(size-1).getQuantity());  // original inventory = 22 - orderItems quantity = 6
    }

    @Test
    public void generateDailySalesReportTest() throws ApiException {
        //to do
        DaySalesPojo daySalesPojo = reportDto.generateDailySalesReport();
        assertEquals((Integer) 2,daySalesPojo.getInvoiced_items_count());
        assertEquals((Double) 9120.0,daySalesPojo.getTotal_revenue());
        assertEquals((Integer) 1,daySalesPojo.getInvoiced_orders_count());
    }

    @Test
    public void getDaySalesReportTest() throws ApiException {
        reportDto.generateDailySalesReport();
        List<DaySalesData> daySalesDataList = reportDto.getDaySalesReport();
        assertEquals(1,daySalesDataList.size());
    }

    private List<OrderItemForm> addMultipleOrderItemToForm(){
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("abc123");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1520.0);

        barcodes.add("abc1234");
        quantities.add(0);
        sellingPrices.add(1520.0);
        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        return orderItemFormList;
    }

}
