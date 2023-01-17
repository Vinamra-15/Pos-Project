package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;
    public List<OrderItemPojo> getByOrderId(Integer id){
        return orderItemDao.selectByOrderId(id);
    }

    public OrderItemPojo getByOrderIdProductId(Integer orderId, Integer productId) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectByOrderIdProductId(orderId,productId);
        if(orderItemPojo==null){
            throw new ApiException("No order item for concerned orderId and productId exists!");
        }
        return orderItemPojo;
    }

    public OrderItemPojo get(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.select(id);
        if(orderItemPojo==null){
            throw new ApiException("No order item for concerned orderId and productId exists!");
        }
        return orderItemPojo;
    }

    public void update(Integer id, OrderItemPojo orderItemPojo) throws ApiException{
        OrderItemPojo exOrderItemPojo = orderItemDao.select(id);
        if(exOrderItemPojo==null){
            throw new ApiException("No order item for concerned orderId and productId exists!");
        }
        exOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
        exOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.update(exOrderItemPojo);
    }

    public void delete(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.select(id);
        if(orderItemPojo==null){
            throw new ApiException("No order item for concerned orderId and productId exists!");
        }
        orderItemDao.delete(id);
    }

    public void add(OrderItemPojo orderItemPojo){
        orderItemDao.insert(orderItemPojo);
    }


}
