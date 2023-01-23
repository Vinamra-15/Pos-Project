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
        brandCategoryService.addBrandCategory(brandCategoryPojo);
    }

    @Test
    public void addProductTest() throws ApiException {
        ProductForm productForm = getProductForm(" some product name "," !@#$23 ",2000.00,"some brand ","some category  ");
        productDto.addProduct(productForm);
        ProductPojo productPojo = productService.getProductByBarcode("!@#$23");
        assertEquals("!@#$23",productPojo.getBarcode());
        assertEquals("some product name",productPojo.getName());
        assertEquals((Double) 2000.00,productPojo.getMrp());
        BrandCategoryPojo brandCategoryPojo = brandCategoryService.getBrandCategory(productPojo.getBrandId());
        assertEquals("some brand",brandCategoryPojo.getBrand());
        assertEquals("some category",brandCategoryPojo.getCategory());
    }

    @Test
    public void addProductNullFieldTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        ProductForm productForm = getProductForm("  "," ",null," ","  ");
        productDto.addProduct(productForm);
    }

    @Test
    public void getProductTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo);
        ProductData productData = productDto.getProduct(productPojo.getId());
        assertEquals("some product name",productData.getName());
        assertEquals((Double) 2000.00,productData.getMrp());
        assertEquals("!@#$23",productData.getBarcode());
        assertEquals("some brand",productData.getBrand());
        assertEquals("some category",productData.getCategory());
    }

    @Test
    public void getInvalidProductTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        ProductData productData = productDto.getProduct(123698745);
    }

    @Test
    public void getProductByBarcodeTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo);
        ProductData productData = productDto.getProductByBarcode("!@#$23");
        assertEquals("some product name",productData.getName());
        assertEquals((Double) 2000.00,productData.getMrp());
        assertEquals("!@#$23",productData.getBarcode());
        assertEquals("some brand",productData.getBrand());
        assertEquals("some category",productData.getCategory());
    }

    @Test
    public void getProductByInvalidBarcodeTest() throws ApiException {
        exceptionRule.expect(ApiException.class);
        ProductData productData = productDto.getProductByBarcode("!@#$23fas21v");
    }

    @Test
    public void getAllProductsTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo);

        ProductPojo productPojo2 = getProductPojo("some other product name","!@#$234",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo2);

        List<ProductData> productDataList = productDto.getAllProducts();
        assertEquals(2,productDataList.size());
    }

    @Test
    public void updateProductTest() throws ApiException {
        ProductPojo productPojo = getProductPojo("some product name","!@#$23",2000.00,brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo);
        // product name updated
        ProductForm productForm = getProductForm("some other product name","!@#$23",2000.00,"some brand","some category");
        productDto.updateProduct(productPojo.getId(),productForm);
        ProductPojo productPojo1 = productService.getProduct(productPojo.getId());
        assertEquals("some other product name",productPojo1.getName());
        assertEquals((Double) 2000.00,productPojo1.getMrp());
        assertEquals("!@#$23",productPojo1.getBarcode());
        assertEquals("some brand",brandCategoryService.getBrandCategory(productPojo1.getBrandId()).getBrand());
        assertEquals("some category",brandCategoryService.getBrandCategory(productPojo1.getBrandId()).getCategory());
    }

}
