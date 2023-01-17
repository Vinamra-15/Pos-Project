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

    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    public OrderPojo get(Integer id) throws ApiException{
        OrderPojo orderPojo = orderDao.select(id);
        if(orderPojo==null)
            throw new ApiException("No order found with Id: " + id);
        return orderPojo;
    }

    public void add(OrderPojo orderPojo){
        orderDao.insert(orderPojo);
    }

    public void update(Integer id, OrderPojo orderPojo) throws ApiException {
        OrderPojo ex = get(id);
        ex.setInvoicePath(orderPojo.getInvoicePath());
        orderDao.update(ex);
    }


    public List<OrderPojo> getByStartDateEndDate(Date startDate, Date endDate) {
        return orderDao.selectByStartDateEndDate(startDate,endDate);
    }
}
