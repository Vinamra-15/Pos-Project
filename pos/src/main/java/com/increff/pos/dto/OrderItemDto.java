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

import static com.increff.pos.util.ConvertUtil.convert;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    public List<OrderItemData> get(Integer id) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getByOrderId(id);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for(OrderItemPojo orderItemPojo:list){
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            list2.add(convert(orderItemPojo,productPojo));
        }
        return list2;
    }
    public OrderItemData getByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.getByOrderIdProductId(orderId,productId);
        ProductPojo productPojo = productService.get(productId);
        return convert(orderItemPojo,productPojo);
    }
}
