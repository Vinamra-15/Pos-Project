package com.increff.pos.dto;


import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Validate.validateForm;


@Component
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    public InventoryData getInventory(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        InventoryPojo inventoryPojo = inventoryService.getInventory(productPojo.getId());
        return convert(inventoryPojo,productPojo);
    }
    public List<InventoryData> getAllInventories() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAllInventories();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo:inventoryPojoList){
            inventoryDataList.add(convert(inventoryPojo,
                    productService.getProduct(inventoryPojo.getProductId())));
        }
        return inventoryDataList;
    }
    public InventoryData updateInventory(String barcode, InventoryForm inventoryForm) throws ApiException {
            validateForm(inventoryForm);
            ProductPojo productPojo = productService.getProductByBarcode(barcode);
            inventoryService.updateInventory(productPojo.getId(), inventoryForm.getQuantity());
            return convert(inventoryService.getInventory(productPojo.getId()),productPojo);
    }
}
