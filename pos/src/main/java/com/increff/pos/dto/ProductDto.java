package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Normalize.normalizeForm;
import static com.increff.pos.util.Validate.validateForm;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private InventoryService inventoryService;


    @Transactional(rollbackFor = ApiException.class)
    public void add(ProductForm productForm) throws ApiException {
        normalizeForm(productForm);
        validateForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckWithBrandCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandCategoryPojo);
        productService.add(productPojo);

        //For every new added product, initialize its quantity in Inventory to Zero.
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
    }

    public ProductData get(Integer id) throws ApiException {
        ProductPojo productPojo = productService.get(id);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(productPojo.getBrandId());
        return convert(productPojo, brandCategoryPojo);
    }

    public ProductData getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(productPojo.getBrandId());
        return convert(productPojo, brandCategoryPojo);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> list = productService.getAll();
        List<ProductData> list2 = new ArrayList<ProductData>();
        for (ProductPojo productPojo : list) {
            BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(productPojo.getBrandId());
            list2.add(convert(productPojo, brandCategoryPojo));
        }
        return list2;
    }

    public void update(Integer id, ProductForm productForm) throws ApiException {
        normalizeForm(productForm);
        validateForm(productForm);

        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getCheckWithBrandCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandCategoryPojo);
        productService.update(id, productPojo);
    }



}
