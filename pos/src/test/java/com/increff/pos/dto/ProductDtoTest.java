package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.increff.pos.util.TestUtils.getProductForm;
import static com.increff.pos.util.TestUtils.getProductPojo;
import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private ProductService productService;

    @Before
    public void addBrandCategory() throws ApiException {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setBrand("some brand");
        brandCategoryPojo.setCategory("some category");

        brandCategoryService.add(brandCategoryPojo);
    }

    @Test
    public void addProductTest() throws ApiException {
        ProductForm productForm = getProductForm(" some product name "," !@#$23 ",2000.00,"some brand ","some category  ");
        productDto.add(productForm);
        ProductPojo productPojo = productService.getByBarcode("!@#$23");
        assertEquals(productPojo.getBarcode(),"!@#$23");
        assertEquals(productPojo.getName(),"some product name");
        assertEquals(productPojo.getMrp(),(Double) 2000.00);
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.get(productPojo.getBrandId());
        assertEquals(brandCategoryPojo.getBrand(),"some brand");
        assertEquals(brandCategoryPojo.getCategory(),"some category");
    }

    @Test(expected = ApiException.class)
    public void addProductNullFieldTest() throws ApiException {
        ProductForm productForm = getProductForm("  "," ",null," ","  ");
        productDto.add(productForm);
        exceptionRule.expect(ApiException.class);
    }

    @Test
    public void getProductTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo);
        ProductData productData = productDto.get(productPojo.getId());
        assertEquals("some product name",productData.getName());
        assertEquals((Double) 2000.00,productData.getMrp());
        assertEquals("!@#$23",productData.getBarcode());
        assertEquals("some brand",productData.getBrand());
        assertEquals("some category",productData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void getInvalidProductTest() throws ApiException {
        ProductData productData = productDto.get(123698745);
        exceptionRule.expect(ApiException.class);
    }

    @Test
    public void getProductByBarcodeTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo);
        ProductData productData = productDto.getByBarcode("!@#$23");
        assertEquals("some product name",productData.getName());
        assertEquals((Double) 2000.00,productData.getMrp());
        assertEquals("!@#$23",productData.getBarcode());
        assertEquals("some brand",productData.getBrand());
        assertEquals("some category",productData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void getProductByInvalidBarcodeTest() throws ApiException {
        ProductData productData = productDto.getByBarcode("!@#$23fas21v");
        exceptionRule.expect(ApiException.class);
    }

    @Test
    public void getAllProductTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo);

        ProductPojo productPojo2 = getProductPojo("some other product name","!@#$234",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo2);

        List<ProductData> productDataList = productDto.getAll();
        assertEquals("some product name",productDataList.get(2).getName());
        assertEquals((Double) 2000.00,productDataList.get(2).getMrp());
        assertEquals("!@#$23",productDataList.get(2).getBarcode());
        assertEquals("some brand",productDataList.get(2).getBrand());
        assertEquals("some category",productDataList.get(2).getCategory());
    }

    @Test
    public void updateProductTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.add(productPojo);
        // product name updated
        ProductForm productForm = getProductForm("some other product name","!@#$23",2000.00,"some brand","some category");
        productDto.update(productPojo.getId(),productForm);
        ProductPojo productPojo1 = productService.get(productPojo.getId());
        assertEquals("some other product name",productPojo1.getName());
        assertEquals((Double) 2000.00,productPojo1.getMrp());
        assertEquals("!@#$23",productPojo1.getBarcode());
        assertEquals("some brand",brandCategoryService.get(productPojo1.getBrandId()).getBrand());
        assertEquals("some category",brandCategoryService.get(productPojo1.getBrandId()).getCategory());
    }

}
