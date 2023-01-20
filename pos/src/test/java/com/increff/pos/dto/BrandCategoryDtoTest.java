package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;
import com.increff.pos.util.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import static org.junit.Assert.assertEquals;


public class BrandCategoryDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private BrandCategoryDto brandCategoryDto;
    @Value("${admin.email}")
    private String name;
    @Test
    public void addBrandCategoryTest() throws ApiException {
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   tShiRt brnd  ","  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);
        BrandCategoryData dataFromDB = brandCategoryDto.get(brandCategoryData.getId());
        System.out.println(name);
        assertEquals("tshirt brnd",dataFromDB.getBrand());
        assertEquals("tshirts",dataFromDB.getCategory());
    }
    @Test
    public void addDuplicateBrandCategoryTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("   tShiRt brnd  "," tshirts ");
        brandCategoryDto.add(brandCategoryForm);
        BrandCategoryForm duplicateBrandCategoryForm = TestUtils.getBrandCategoryForm("tshirt brnd","tshirts");
        brandCategoryDto.add(duplicateBrandCategoryForm);

    }
    @Test
    public void addSpacesInBrandCategoryTest() throws ApiException {  // inputs blank spaces
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("     ","");
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);
    }
    @Test
    public void addNullBrandTest() throws ApiException{
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(null,"  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);
    }
    @Test
    public void addNullCategoryTest() throws ApiException{
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" Brands  ",null);
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);

    }
    @Test
    public void getAllBrandCategoryTest() throws ApiException {
        BrandCategoryForm firstBrandCategoryForm = TestUtils.getBrandCategoryForm("   tshirt brnd  ","  tshirts  ");
        brandCategoryDto.add(firstBrandCategoryForm);
        BrandCategoryForm secondBrandCategoryForm = TestUtils.getBrandCategoryForm("jeans brnd  ","  jeans  ");
        brandCategoryDto.add(secondBrandCategoryForm);
        List<BrandCategoryData> data = brandCategoryDto.getAll();
//        System.out.println(data);
        assertEquals(data.get(0).getBrand(),"tshirt brnd");
        assertEquals(data.get(1).getBrand(),"jeans brnd");
        assertEquals(data.get(0).getCategory(),"tshirts");
        assertEquals(data.get(1).getCategory(),"jeans");
    }
    @Test
    public void updateBrandCategoryTest() throws ApiException {
//        BrandCategoryData brandCategoryDataClone = brandCategoryDto.get(1);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm(" tshirt brnd ","  tshirsfats  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);
        BrandCategoryForm updatedBrandCategoryForm = TestUtils.getBrandCategoryForm(" tshirt brand ","  tshirts ");
        brandCategoryDto.update(brandCategoryData.getId(), updatedBrandCategoryForm);
        BrandCategoryData data = brandCategoryDto.get(brandCategoryData.getId());
        assertEquals("tshirt brand",data.getBrand());
        assertEquals("tshirts",data.getCategory());
    }
    @Test
    public void searchInvalidIdTestId() throws ApiException {
        exceptionRule.expect(ApiException.class);
        BrandCategoryForm brandCategoryForm = TestUtils.getBrandCategoryForm("  some name   ","  tshirts  ");
        BrandCategoryData brandCategoryData = brandCategoryDto.add(brandCategoryForm);
        BrandCategoryData data = brandCategoryDto.get(5);
    }
}
