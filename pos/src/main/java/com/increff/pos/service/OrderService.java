package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.TimeUtil;
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
        if(startDate==null&&endDate==null){
            return orderDao.selectAll();
        }
        else if(startDate==null){
            endDate = TimeUtil.getEndOfDay(endDate);
            return orderDao.selectBefore(endDate);

        }
        else if(endDate==null){
            startDate = TimeUtil.getStartOfDay(startDate);
            return orderDao.selectAfter(startDate);
        }
        else
        {
            startDate = TimeUtil.getStartOfDay(startDate);
            endDate = TimeUtil.getEndOfDay(endDate);
            return orderDao.selectByStartDateEndDate(startDate,endDate);
        }

    }
}
