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

import java.util.List;
import java.util.stream.Collectors;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Normalize.normalizePojo;
import static com.increff.pos.util.Validate.validateForm;

@Component
public class ProductDto {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandCategoryService brandCategoryService;
    @Autowired
    private InventoryService inventoryService;



    //For every new added product, initialize its quantity in Inventory to Zero.
    @Transactional(rollbackFor = ApiException.class)
    public void addProduct(ProductForm productForm) throws ApiException {
        validateForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getByBrandCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandCategoryPojo);
        productService.addProduct(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(0);
        inventoryService.addInventory(inventoryPojo);
    }

    public ProductData getProduct(Integer id) throws ApiException {
        ProductPojo productPojo = productService.getProduct(id);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(productPojo.getBrandId());
        return convert(productPojo, brandCategoryPojo);
    }

    public ProductData getProductByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getProductByBarcode(barcode);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(productPojo.getBrandId());
        return convert(productPojo, brandCategoryPojo);
    }

    public List<ProductData> getAllProducts() throws ApiException {
        return productService.getAllProducts()
                .stream()
                .map(productPojo -> {
                    try {
                        return convert(productPojo
                                ,brandCategoryService.getBrandCategory(productPojo.getBrandId()));
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public void updateProduct(Integer id, ProductForm productForm) throws ApiException {
        validateForm(productForm);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getByBrandCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandCategoryPojo);
        productService.updateProduct(id, productPojo);
    }
}
