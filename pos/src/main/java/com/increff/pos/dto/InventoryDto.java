package com.increff.pos.dto;


import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Validate.validateForm;


@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    public InventoryData get(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
        return convert(inventoryPojo,productPojo);
    }
    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> list = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : list) {
            ProductPojo productPojo = productService.get(inventoryPojo.getProductId());
            list2.add(convert(inventoryPojo,productPojo));
        }
        return list2;
    }
    public void update(String barcode, InventoryForm inventoryForm) throws ApiException {
        try{
            validateForm(inventoryForm);
            ProductPojo productPojo = productService.getByBarcode(barcode);
            InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo);
            inventoryService.update(productPojo.getId(), inventoryPojo);

        }
        catch (Exception e)
        {
            throw new ApiException(e.toString());
        }
    }
}
