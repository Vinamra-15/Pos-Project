package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.pos.util.ConvertUtil.convert;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    public List<OrderItemData> get(Integer id) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.getOrderItemsByOrderId(id);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo:orderItemPojoList){
            orderItemDataList.add(convert(orderItemPojo,productService.getProduct(orderItemPojo.getProductId())));
        }
        return orderItemDataList;
    }
    public OrderItemData getByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.getOrderItemByOrderIdProductId(orderId,productId);
        ProductPojo productPojo = productService.getProduct(productId);
        return convert(orderItemPojo,productPojo);
    }
}
