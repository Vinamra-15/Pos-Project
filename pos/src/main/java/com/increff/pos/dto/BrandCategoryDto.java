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
import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Normalize.normalizeForm;
import static com.increff.pos.util.Validate.validateForm;

@Component
public class BrandCategoryDto {

    @Autowired
    private BrandCategoryService brandCategoryService;
    public void add(BrandCategoryForm brandCategoryForm) throws ApiException {
        normalizeForm(brandCategoryForm);
        validateForm(brandCategoryForm);
        BrandCategoryPojo p = ConvertUtil.convert(brandCategoryForm);
		brandCategoryService.add(p);
    }

    public BrandCategoryData get(Integer id) throws ApiException {
        BrandCategoryPojo pojo = brandCategoryService.get(id);
        return convert(pojo);
    }

    public List<BrandCategoryData> getAll()
    {
        List<BrandCategoryPojo> list = brandCategoryService.getAll();
        List<BrandCategoryData> list2 = new ArrayList<BrandCategoryData>();
        for (BrandCategoryPojo p : list) {
            list2.add(convert(p));
        }
        Collections.reverse(list2);
        return list2;
    }

    public void update(Integer id, BrandCategoryForm form) throws ApiException {
        normalizeForm(form);
        validateForm(form);
        BrandCategoryPojo p = ConvertUtil.convert(form);
        brandCategoryService.update(id, p);
    }

}
