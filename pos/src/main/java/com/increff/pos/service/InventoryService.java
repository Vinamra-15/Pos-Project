package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    public void addInventory(InventoryPojo inventoryPojo) throws ApiException {
        inventoryDao.insert(inventoryPojo);
    }

    public InventoryPojo getInventory(Integer productId) throws ApiException {
        return getCheck(productId);
    }

    public List<InventoryPojo> getAllInventories() {
        return inventoryDao.selectAll();
    }


    public void updateInventory(Integer productId, InventoryPojo inventoryPojo) throws ApiException {
        if(inventoryPojo.getQuantity()<0)
            throw new ApiException("Quantity should be non negative number!");
        InventoryPojo existing = getCheck(productId);
        existing.setProductId(inventoryPojo.getProductId());
        existing.setQuantity(inventoryPojo.getQuantity());
        inventoryDao.update(existing);
    }

    public InventoryPojo getCheck(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (inventoryPojo == null) {
            throw new ApiException("Product does not exist!");
        }
        return inventoryPojo;
    }

    public void validateAndReduceInventoryQuantity(Integer productId, Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = getInventory(productId);
        if(quantity>inventoryPojo.getQuantity())
        {
            throw new ApiException("Insufficient Inventory!");
        }
        Integer newQuantity = inventoryPojo.getQuantity() - quantity;
        inventoryPojo.setQuantity(newQuantity);
        updateInventory(productId,inventoryPojo);
    }
}
