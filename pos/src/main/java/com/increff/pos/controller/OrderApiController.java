package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderDetailsData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public void add(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        orderDto.createOrder(orderItemForms);
    }

    @ApiOperation(value = "Gets an order")
    @RequestMapping(path = "/api/orders/{id}",method = RequestMethod.GET)
    public OrderDetailsData getOrderDetails(@PathVariable Integer id) throws ApiException {
        return orderDto.getOrderDetails(id);
    }

    @ApiOperation(value = "Gets all orders")
    @RequestMapping(path = "/api/orders",method = RequestMethod.GET)
    public List<OrderData> getAllOrders() throws ApiException {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Updates an order")
    @RequestMapping(path = "/api/orders/{id}",method = RequestMethod.PUT)
    public void getOrderDetails(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItemForms) throws ApiException, IOException {
        orderDto.update(id,orderItemForms);
    }
}
