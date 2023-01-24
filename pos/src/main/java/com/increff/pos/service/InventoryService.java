package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.StringUtil;
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


    public void updateInventory(Integer productId, Integer quantity) throws ApiException {
        if(quantity<0)
            throw new ApiException("Quantity should be non negative number!");
        InventoryPojo existing = getCheck(productId);
        existing.setQuantity(quantity);
        inventoryDao.update(existing);
    }

    public void reduceInventory(String barcode, Integer productId, Integer newQuantity) throws ApiException {
        barcode = StringUtil.toLowerCase(barcode);
        Integer existingQuantity = getCheck(productId).getQuantity();
        if(existingQuantity<newQuantity)
            throw new ApiException("Insufficient inventory for product with barcode: " + barcode);
        updateInventory(productId,existingQuantity-newQuantity);
    }

    private InventoryPojo getCheck(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (inventoryPojo == null) {
            throw new ApiException("Product does not exist!");
        }
        return inventoryPojo;
    }

    public void increaseByQuantity(Integer productId, Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = getInventory(productId);
        Integer existingQuantity = inventoryPojo.getQuantity();
        Integer newQuantity = existingQuantity + quantity;
        updateInventory(productId,newQuantity);
    }

//    public void validateAndReduceInventoryQuantity(Integer productId, Integer quantity) throws ApiException {
//        InventoryPojo inventoryPojo = getInventory(productId);
//        if(quantity>inventoryPojo.getQuantity())
//        {
//            throw new ApiException("Insufficient Inventory!");
//        }
//        Integer newQuantity = inventoryPojo.getQuantity() - quantity;
//        inventoryPojo.setQuantity(newQuantity);
//        updateInventory(productId,inventoryPojo);
//    }
}
