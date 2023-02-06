package com.increff.pos.util;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Test;

import static com.increff.pos.util.Normalize.normalizePojo;
import static org.junit.Assert.assertEquals;

public class NormalizeTest extends AbstractUnitTest {

    @Test
    public void normalizeBrandCategoryPojoTest(){
        BrandCategoryPojo brandCategoryPojo = TestUtils.getBrandCategoryPojo(1," brAnd  ","CaTegory  ");
        normalizePojo(brandCategoryPojo);
        assertEquals("brand",brandCategoryPojo.getBrand());
        assertEquals("category",brandCategoryPojo.getCategory());
    }

    @Test
    public void normalizeProductPojoTest(){
        ProductPojo productPojo = TestUtils.getProductPojo(" produCt  "," a11RT  ",1200.00111, 1);
        normalizePojo(productPojo);
        assertEquals("product",productPojo.getName());
        assertEquals("a11rt",productPojo.getBarcode());
        assertEquals((Double) 1200.00,productPojo.getMrp());
    }


    @Test
    public void normalizeOrderItemPojoTest(){
        OrderItemPojo orderItemPojo = TestUtils.getOrderItemPojo(1,1,1,100.2111);
        normalizePojo(orderItemPojo);
        assertEquals((Double) 100.21,orderItemPojo.getSellingPrice());
    }


}
