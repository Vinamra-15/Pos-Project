package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController {

    @Autowired
    private ProductDto productDto;
    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/products", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException {
        productDto.add(form);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        return productDto.get(id);
    }
    @ApiOperation(value = "Gets a product by barcode")
    @RequestMapping(path = "/api/products",params = "barcode", method = RequestMethod.GET)
    public ProductData get(@RequestParam("barcode") String barcode) throws ApiException {
        return productDto.getByBarcode(barcode);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/api/products",method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException {
        productDto.update(id,form);
    }

}
