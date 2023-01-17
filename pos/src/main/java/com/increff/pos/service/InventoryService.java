package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    public void add(InventoryPojo inventoryPojo) throws ApiException {
        dao.insert(inventoryPojo);
    }

    public InventoryPojo get(Integer productId) throws ApiException {
        return getCheck(productId);
    }

    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }


    public void update(Integer productId, InventoryPojo inventoryPojo) throws ApiException {

        InventoryPojo ex = getCheck(productId);
        ex.setProductId(inventoryPojo.getProductId());
        ex.setQuantity(inventoryPojo.getQuantity());
        dao.update(ex);
    }

    public InventoryPojo getCheck(Integer productId) throws ApiException {
        InventoryPojo p = dao.select(productId);
        if (p == null) {
            throw new ApiException("Product with given barcode does not exist");
        }
        return p;
    }
}
