package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public List<OrderPojo> getAllOrders(){
        return orderDao.selectAll();
    }

    public OrderPojo getOrder(Integer orderId) throws ApiException{
        OrderPojo orderPojo = orderDao.select(orderId);
        if(orderPojo==null)
            throw new ApiException("No order found with Id: " + orderId);
        return orderPojo;
    }

    public void addOrder(OrderPojo orderPojo){
        orderDao.insert(orderPojo);
    }

    public void updateOrder(Integer id, OrderPojo orderPojo) throws ApiException {
        OrderPojo ex = getOrder(id);
        ex.setInvoicePath(orderPojo.getInvoicePath());
        orderDao.update(ex);
    }


    public List<OrderPojo> getOrderByStartDateEndDate(Date startDate, Date endDate) {
        return orderDao.selectByStartDateEndDate(startDate,endDate);
    }
}
