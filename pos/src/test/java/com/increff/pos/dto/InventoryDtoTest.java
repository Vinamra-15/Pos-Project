package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.helper.TestUtils.*;
import static org.junit.Assert.assertEquals;

public class InventoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Autowired
    private ProductDto productDto;

    @Before
    public void addProduct() throws ApiException {
        BrandCategoryForm brandCategoryForm = getBrandCategoryForm("some brand","some category ");
        ProductForm productForm = getProductForm("some tshirt  "," $!@11" , 2222.0,"some brand ","some category  " );
        brandCategoryDto.addBrandCategory(brandCategoryForm);
        productDto.addProduct(productForm);
    }
    @Test
    public void getTest() throws ApiException {
        InventoryData inventoryData = inventoryDto.getInventory(" $!@11  ");
        assertEquals("$!@11",inventoryData.getBarcode());
        assertEquals("some tshirt",inventoryData.getProductName());
        assertEquals((Integer) 0,inventoryData.getQuantity());
    }

    @Test
    public void getAllTest() throws ApiException {
        List<InventoryData> list = inventoryDto.getAllInventories();
        assertEquals(1,list.size());
    }

    @Test
    public void updateTest() throws ApiException {
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",2);
        InventoryData inventoryData = inventoryDto.updateInventory(" $!@11  ",inventoryForm);
        assertEquals((Integer) 2,inventoryData.getQuantity());
        assertEquals("$!@11",inventoryData.getBarcode());
        assertEquals("some tshirt",inventoryData.getProductName());
    }

    @Test
    public void invalidBarcodeGetTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        InventoryData inventoryData= inventoryDto.getInventory("     ");
    }

    @Test
    public void invalidBarcodeUpdateTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",2);
        InventoryData inventoryData = inventoryDto.updateInventory("     ",inventoryForm);
    }

    @Test
    public void quantityTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",-2);
        InventoryData inventoryData = inventoryDto.updateInventory("   $!@11    ",inventoryForm);
    }

    @Test
    public void nullBarcodeQuantityTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        InventoryForm inventoryForm = getInventoryForm(null,null);
        InventoryData inventoryData = inventoryDto.updateInventory("   $!@11    ",inventoryForm);
    }

}
