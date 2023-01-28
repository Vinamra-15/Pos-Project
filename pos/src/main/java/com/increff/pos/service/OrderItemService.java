package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.increff.pos.util.Normalize.normalizePojo;

@Service
@Transactional
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;
    public List<OrderItemPojo> getOrderItemsByOrderId(Integer orderId){
        return orderItemDao.selectByOrderId(orderId);
    }
    public OrderItemPojo getOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = getCheckOrderItem(id);
        return orderItemPojo;
    }
    public OrderItemPojo getOrderItemByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectByOrderIdProductId(orderId,productId);
        if(orderItemPojo==null){
            throw new ApiException("No order item for orderId: " + orderId + "and productId: " + productId + "exists!");
        }
        return orderItemPojo;
    }
    public void addOrderItem(OrderItemPojo orderItemPojo){
        normalizePojo(orderItemPojo);
        orderItemDao.insert(orderItemPojo);
    }
    public void updateOrderItem(Integer id, OrderItemPojo orderItemPojo) throws ApiException{
        normalizePojo(orderItemPojo);
        OrderItemPojo exOrderItemPojo = getCheckOrderItem(id);
        exOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.update(exOrderItemPojo);
    }
    public void deleteOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = getCheckOrderItem(id);
        orderItemDao.delete(id);
    }
    public void deleteByOrderId(Integer orderId){
        orderItemDao.deleteByOrderId(orderId);
    }
    private OrderItemPojo getCheckOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.select(id);
        if(orderItemPojo==null){
            throw new ApiException("Order item does not exist!");
        }
        return orderItemPojo;
    }
}
