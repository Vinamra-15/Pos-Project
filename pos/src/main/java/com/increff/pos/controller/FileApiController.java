package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api
@RestController
public class FileApiController {
    @Autowired
    private OrderDto orderDto;
    @RequestMapping(path = "/download/invoice/{orderId}", method = RequestMethod.GET)
    public Resource download(@PathVariable Integer orderId) throws ApiException {
        try{
              return orderDto.getFileResource(orderId);
        }
        catch (IOException exception){
            throw new ApiException("Pdf being generated!");
        }
    }
}
