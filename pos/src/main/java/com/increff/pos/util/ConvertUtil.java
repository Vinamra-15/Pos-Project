package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    public static BrandCategoryData convert(BrandCategoryPojo brandCategoryPojo) {
        BrandCategoryData brandCategoryData = new BrandCategoryData();
        brandCategoryData.setCategory(brandCategoryPojo.getCategory());
        brandCategoryData.setBrand(brandCategoryPojo.getBrand());
        brandCategoryData.setId(brandCategoryPojo.getId());
        return brandCategoryData;
    }
    public static BrandCategoryPojo convert(BrandCategoryForm brandCategoryForm) {
        BrandCategoryPojo brandCategoryPojo = new BrandCategoryPojo();
        brandCategoryPojo.setCategory(brandCategoryForm.getCategory());
        brandCategoryPojo.setBrand(brandCategoryForm.getBrand());
        return brandCategoryPojo;
    }
    public static ProductData convert(ProductPojo productPojo, BrandCategoryPojo brandCategoryPojo) throws ApiException {
        ProductData productData = new ProductData();
        productData.setName(productPojo.getName());
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(brandCategoryPojo.getBrand());
        productData.setCategory(brandCategoryPojo.getCategory());
        return productData;
    }
    public static ProductPojo convert(ProductForm productForm, BrandCategoryPojo brandCategoryPojo) throws ApiException {

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandId(brandCategoryPojo.getId());
        return productPojo;
    }

    public static InventoryData convert(InventoryPojo inventoryPojo, ProductPojo productPojo) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm, ProductPojo productPojo) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        inventoryPojo.setProductId(productPojo.getId());
        return inventoryPojo;
    }

    public static OrderItemData convert(OrderItemPojo orderItemPojo,ProductPojo productPojo){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setName(productPojo.getName());
        orderItemData.setBarcode(productPojo.getBarcode());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemData;
    }

    public static OrderItemPojo convert(OrderItemForm orderItemForm, ProductPojo productPojo, OrderPojo orderPojo){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setOrderId(orderPojo.getId());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return orderItemPojo;
    }



    public static OrderData convert(OrderPojo orderPojo){
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setDatetime(orderPojo.getDatetime());
        orderData.setInvoicePath(orderPojo.getInvoicePath());
        return orderData;
    }

    public static List<DaySalesData> convert(List<DaySalesPojo> daySalesPojoList){
        List<DaySalesData> daySalesDataList = new ArrayList<DaySalesData>();
        for(DaySalesPojo daySalesPojo:daySalesPojoList){
            DaySalesData daySalesData = new DaySalesData();
            daySalesData.setDate(daySalesPojo.getDate());
            daySalesData.setInvoiced_items_count(daySalesPojo.getInvoiced_items_count());
            daySalesData.setInvoiced_orders_count(daySalesPojo.getInvoiced_orders_count());
            daySalesData.setTotal_revenue(daySalesPojo.getTotal_revenue());
            daySalesDataList.add(daySalesData);
        }

        return daySalesDataList;
    }

    public static UserPojo convert(SignUpForm signUpForm){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(signUpForm.getEmail());
        userPojo.setPassword(signUpForm.getPassword());
        return userPojo;
    }

    public static Authentication convert(UserPojo p,String adminEmail) {
        // Create principal
        UserPrincipal principal = new UserPrincipal();
        principal.setEmail(p.getEmail());
        principal.setId(p.getId());

        // Create Authorities
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

        if(adminEmail.equals(p.getEmail()))
        authorities.add(new SimpleGrantedAuthority("supervisor"));
        else{
            authorities.add(new SimpleGrantedAuthority("operator"));
        }
        // you can add more roles if required

        // Create Authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
                authorities);
        return token;
    }

    public static UserData convert(UserPojo p) {
        UserData d = new UserData();
        d.setEmail(p.getEmail());
        d.setRole(p.getRole());
        d.setId(p.getId());
        return d;
    }

    public static UserPojo convert(UserForm f) {
        UserPojo p = new UserPojo();
        p.setEmail(f.getEmail());
        p.setRole(f.getRole());
        p.setPassword(f.getPassword());
        return p;
    }
}
