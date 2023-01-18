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

import static com.increff.pos.util.TestUtils.*;
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
        brandCategoryDto.add(brandCategoryForm);
        productDto.add(productForm);
    }
    @Test
    public void getTest() throws ApiException {
        InventoryData inventoryData = inventoryDto.get(" $!@11  ");
        assertEquals(inventoryData.getBarcode(),"$!@11");
        assertEquals(inventoryData.getProductName(),"some tshirt");
        assertEquals(inventoryData.getQuantity(),(Integer) 0);
    }

    @Test
    public void getAllTest() throws ApiException {
        List<InventoryData> list = inventoryDto.getAll();
//        assertEquals(list.size(),3);
    }

    @Test
    public void updateTest() throws ApiException {
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",2);
        InventoryData inventoryData = inventoryDto.update(" $!@11  ",inventoryForm);
        assertEquals(inventoryData.getQuantity(),(Integer) 2);
        assertEquals(inventoryData.getBarcode(),"$!@11");
        assertEquals(inventoryData.getProductName(),"some tshirt");
    }

    @Test(expected = ApiException.class)
    public void invalidBarcodeGetTest() throws ApiException {
        InventoryData inventoryData= inventoryDto.get("     ");
        exceptionRule.expect(ApiException.class);
    }

    @Test(expected = ApiException.class)
    public void invalidBarcodeUpdateTest() throws ApiException {
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",2);
        InventoryData inventoryData = inventoryDto.update("     ",inventoryForm);
        exceptionRule.expect(ApiException.class);
    }

    @Test(expected = ApiException.class)
    public void quantityTest() throws ApiException {
        InventoryForm inventoryForm = getInventoryForm(" $!@11 ",-2);
        InventoryData inventoryData = inventoryDto.update("   $!@11    ",inventoryForm);
        exceptionRule.expect(ApiException.class);
    }

    @Test(expected = ApiException.class)
    public void nullBarcodeQuantityTest() throws ApiException {
        InventoryForm inventoryForm = getInventoryForm(null,null);
        InventoryData inventoryData = inventoryDto.update("   $!@11    ",inventoryForm);
        exceptionRule.expect(ApiException.class);
    }

}
