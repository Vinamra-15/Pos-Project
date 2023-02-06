package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.Data.InventoryData;
import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
public class InventoryApiController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Gets a product-quantity detail by ID")
    @RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.GET)
    public InventoryData getInventory(@PathVariable String barcode) throws ApiException {
        return inventoryDto.getInventory(barcode);
    }

    @ApiOperation(value = "Gets list of all product-quantity details")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAllInventories() throws ApiException {
        return inventoryDto.getAllInventories();
    }

    @ApiOperation(value = "Updates a product-quantity detail")
    @RequestMapping(path = {"/api/inventory","/api/inventory/{barcode}"}, method = RequestMethod.PUT)
    public void updateInventory(@PathVariable Optional<String> barcode, @RequestBody InventoryForm inventoryForm) throws ApiException {
        if(barcode.isPresent())
            inventoryDto.updateInventory(barcode.get(),inventoryForm);
        else
            throw new ApiException("Please enter valid barcode!");
    }
}
