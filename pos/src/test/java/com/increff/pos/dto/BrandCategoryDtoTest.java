package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.helper.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.junit.Assert.assertEquals;


public class BrandCategoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Test
    public void addBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   tShiRt brnd  ","  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);
        BrandCategoryData dataFromDB = brandCategoryDto.getBrandCategory(brandCategoryData.getId());
        assertEquals("tshirt brnd",dataFromDB.getBrand());
        assertEquals("tshirts",dataFromDB.getCategory());
    }
    @Test
    public void addDuplicateBrandCategoryTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   tShiRt brnd  "," tshirts ");
        brandCategoryDto.addBrandCategory(brandCategoryForm);
        BrandCategoryForm duplicateBrandCategoryForm = TestUtils.getBrandCategoryForm("tshirt brnd","tshirts");
        brandCategoryDto.addBrandCategory(duplicateBrandCategoryForm);
    }
    @Test
    public void addSpacesInBrandCategoryTest() throws ApiException {  // inputs blank spaces
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("     ","");
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);
    }
    @Test
    public void addNullBrandTest() throws ApiException{
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null,"  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);
    }
    @Test
    public void addNullCategoryTest() throws ApiException{
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" Brands  ",null);
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);

    }
    @Test
    public void getAllBrandCategoryTest() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   tshirt brnd  ","  tshirts  ");
        brandCategoryDto.addBrandCategory(firstBrandCategoryForm);
        BrandCategoryForm secondBrandCategoryForm = TestUtils.getBrandCategoryForm("jeans brnd  ","  jeans  ");
        brandCategoryDto.addBrandCategory(secondBrandCategoryForm);
        List<BrandCategoryData> data = brandCategoryDto.getAllBrandCategory();
        assertEquals(2,data.size());
    }
    @Test
    public void updateBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" tshirt brnd ","  tshirsfats  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);
        BrandCategoryForm updatedBrandCategoryForm = TestUtils.getBrandCategoryForm(" tshirt brand ","  tshirts ");
        brandCategoryDto.update(brandCategoryData.getId(), updatedBrandCategoryForm);
        BrandCategoryData data = brandCategoryDto.getBrandCategory(brandCategoryData.getId());
        assertEquals("tshirt brand",data.getBrand());
        assertEquals("tshirts",data.getCategory());
    }
    @Test
    public void searchInvalidIdTestId() throws ApiException {
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("  some name   ","  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.addBrandCategory(brandCategoryForm);
        BrandCategoryData data = brandCategoryDto.getBrandCategory(5);
    }
}
