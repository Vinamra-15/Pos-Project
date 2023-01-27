package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderDetailsData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

@Api
@RestController
public class OrderApiController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Adds an order")
    @RequestMapping(path = "/api/orders",method = RequestMethod.POST)
    public void addOrder(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        orderDto.createOrder(orderItemForms);
    }

    @ApiOperation(value = "Gets detail of an order")
    @RequestMapping(path = "/api/orders/{id}",method = RequestMethod.GET)
    public OrderDetailsData getOrderDetails(@PathVariable Integer id) throws ApiException {
        return orderDto.getOrderDetails(id);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "/api/orders",method = RequestMethod.GET)
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Updates an order")
    @RequestMapping(path = "/api/orders/{id}",method = RequestMethod.PUT)
    public void updateOrder(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        orderDto.updateOrder(id,orderItemForms);
    }
}
