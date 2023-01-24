package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return orderItemService.getOrderItemsByOrderId(id)
                .stream()
                .map(orderItemPojo-> {
                    try {
                        return convert(orderItemPojo,productService.getProduct(orderItemPojo.getProductId()));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
//        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
//        for(OrderItemPojo orderItemPojo:list){
//            ProductPojo productPojo = productService.getProduct(orderItemPojo.getProductId());
//            list2.add(convert(orderItemPojo,productPojo));
//        }
//        return list2;
    }
    public OrderItemData getByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.getOrderItemByOrderIdProductId(orderId,productId);
        ProductPojo productPojo = productService.getProduct(productId);
        return convert(orderItemPojo,productPojo);
    }
}
