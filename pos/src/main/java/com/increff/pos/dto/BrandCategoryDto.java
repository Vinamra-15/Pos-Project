package com.increff.pos.dto;

import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
import com.increff.pos.pojo.BrandCategoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandCategoryService;
import com.increff.pos.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Normalize.normalizePojo;
import static com.increff.pos.util.Validate.validateForm;

@Component
public class BrandCategoryDto {

    @Autowired
    private BrandCategoryService brandCategoryService;
    public BrandCategoryData addBrandCategory(BrandCategoryForm brandCategoryForm) throws ApiException {
        validateForm(brandCategoryForm);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convert(brandCategoryForm);
		brandCategoryService.addBrandCategory(brandCategoryPojo);
        return convert(brandCategoryPojo);
    }

    public BrandCategoryData getBrandCategory(Integer id) throws ApiException {
        BrandCategoryPojo pojo = brandCategoryService.getBrandCategory(id);
        return convert(pojo);
    }

    public List<BrandCategoryData> getAllBrandCategory()
    {
        List<BrandCategoryData> brandCategoryDataList = brandCategoryService.getAllBrandCategory()
                .stream()
                .map(ConvertUtil::convert)
                .collect(Collectors.toList());
        return brandCategoryDataList;
    }

    public BrandCategoryData update(Integer id, BrandCategoryForm form) throws ApiException {
        validateForm(form);
        BrandCategoryPojo brandCategoryPojo = ConvertUtil.convert(form);
        brandCategoryService.update(id, brandCategoryPojo);
        return convert(brandCategoryPojo);
    }



}
