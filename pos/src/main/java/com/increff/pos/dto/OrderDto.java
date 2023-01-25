package com.increff.pos.dto;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderDetailsData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.pos.util.ConvertUtil.*;
import static com.increff.pos.util.Normalize.normalizePojo;
import static com.increff.pos.util.PdfUtil.generatePDF;
import static com.increff.pos.util.Validate.validateForm;

@Component
@Transactional(rollbackFor = {ClassCastException.class, ApiException.class, Throwable.class})
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    /*
            Create Order
            1. productPojo(fetch product pojo from each orderItemForm's barcode)
            2.For each productPojo since barcode existence is already checked, compare and reduce inventory quantity
                i. reduceInventoryQuantity(orderItemPojo,productPojo)
            2. create new orderPojo
            3. Save orderItemPojo from convertUtil using(orderItemForm,productPojo,orderPojo) for each orderItemForm
            4. get time and set in orderPojo
            5. save orderPojo in order table
     */
    public OrderData createOrder(List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        validateForm(orderItemForms);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDatetime(Date.from(Instant.now()));
        orderPojo.setInvoicePath(null);
        orderService.addOrder(orderPojo);
        addOrderItems(orderItemForms,orderPojo);
        savePdf(orderPojo);
        return convert(orderPojo);
    }
    public void updateOrder(Integer orderId, List<OrderItemForm> orderItems) throws ApiException, IOException {
        validateForm(orderItems);
        revertInventory(orderId);
        orderItemService.deleteByOrderId(orderId);
        OrderPojo orderPojo = orderService.getOrder(orderId);
        addOrderItems(orderItems,orderPojo);
        savePdf(orderPojo);
    }
    public List<OrderData> getAllOrders() {
        return orderService.getAllOrders().stream().map(ConvertUtil::convert).collect(Collectors.toList());
    }
    public OrderDetailsData getOrderDetails(Integer orderId) throws ApiException {
        OrderDetailsData orderDetailsData = new OrderDetailsData();
        OrderPojo orderPojo = orderService.getOrder(orderId);
        List<OrderItemPojo> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        List<ProductPojo> productPojos = getProductsFromOrderItemPojoList(orderItems);
        List<OrderItemData> orderItemDataList = getOrderItemDetailsList(orderItems,productPojos);
        orderDetailsData.setId(orderPojo.getId());
        orderDetailsData.setDatetime(orderPojo.getDatetime());
        orderDetailsData.setItems(orderItemDataList);
        return orderDetailsData;
    }
    public Resource getFileResource(Integer orderId) throws ApiException, IOException {
        OrderPojo orderPojo = orderService.getOrder(orderId);
        String billDirPath =  orderPojo.getInvoicePath();   //"D:\\IncreffProjectPOS BILLS\\bills\\" + orderId + ".pdf";
        File file = new File(billDirPath);
        Path path = Paths.get(file.getAbsolutePath());
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    private void revertInventory(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsByOrderId(orderId);
        for(OrderItemPojo orderItemPojo:orderItemPojoList){
            inventoryService.increaseByQuantity(orderItemPojo.getProductId(),orderItemPojo.getQuantity());
        }
    }
    private List<ProductPojo> getProductsFromOrderItemPojoList(List<OrderItemPojo> orderItems) throws ApiException {
        List<ProductPojo> productPojos = new ArrayList<ProductPojo>();
        for(OrderItemPojo orderItemPojo:orderItems){
            ProductPojo productPojo = productService.getProduct(orderItemPojo.getProductId());
            productPojos.add(productPojo);
        }
        return productPojos;
    }

    private void addOrderItems(List<OrderItemForm> orderItemForms, OrderPojo orderPojo) throws ApiException {
        for(OrderItemForm orderItemForm:orderItemForms){
            ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
            OrderItemPojo orderItemPojo = convert(orderItemForm, productPojo, orderPojo);
            inventoryService.reduceInventory(orderItemForm.getBarcode(),productPojo.getId(),orderItemForm.getQuantity());
            orderItemService.addOrderItem(orderItemPojo);
        }
    }
    private void savePdf(OrderPojo orderPojo) throws ApiException, IOException {
        String invoicePath = generatePDF(getOrderDetails(orderPojo.getId()));;
        orderPojo.setInvoicePath(invoicePath);
        orderService.updateOrder(orderPojo.getId(),orderPojo);
    }

}

