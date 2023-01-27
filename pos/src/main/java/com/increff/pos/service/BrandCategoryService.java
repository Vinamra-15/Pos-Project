package com.increff.pos.service;

import java.util.Collections;
import java.util.List;

import com.increff.pos.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandCategoryDao;
import com.increff.pos.pojo.BrandCategoryPojo;

import static com.increff.pos.util.Normalize.normalizePojo;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BrandCategoryService {

	@Autowired
	private BrandCategoryDao brandCategoryDao;

	public void addBrandCategory(BrandCategoryPojo brandCategoryPojo) throws ApiException {
		normalizePojo(brandCategoryPojo);
		checkBrandCatDuplicateExists(brandCategoryPojo.getBrand(),brandCategoryPojo.getCategory());
		brandCategoryDao.insert(brandCategoryPojo);
	}

	public BrandCategoryPojo getBrandCategory(Integer id) throws ApiException {
		return getCheck(id);
	}
	public List<BrandCategoryPojo> getAllBrandCategory() {
		return brandCategoryDao.selectAll();
	}

	public List<BrandCategoryPojo> getBrandCategoryByBrand(String brand) {
		brand = StringUtil.toLowerCase(brand);
		return brandCategoryDao.selectByBrand(brand);
	}

	public List<BrandCategoryPojo> getBrandCategoryByCategory(String category) {
		category = StringUtil.toLowerCase(category);
		return brandCategoryDao.selectByCategory(category);
	}

	public BrandCategoryPojo getByBrandCategory(String brand,String category) throws ApiException {
		brand = StringUtil.toLowerCase(brand);
		category = StringUtil.toLowerCase(category);
		BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(brand,category);
		nullBrandCategoryPojoException(brandCategoryPojo);
		return brandCategoryPojo;
	}

	public void update(Integer id, BrandCategoryPojo brandCategoryPojo) throws ApiException {
		normalizePojo(brandCategoryPojo);
		checkBrandCatDuplicateExists(brandCategoryPojo.getBrand(),brandCategoryPojo.getCategory());
		BrandCategoryPojo existingBrandCategoryPojo = getCheck(id);
		existingBrandCategoryPojo.setCategory(brandCategoryPojo.getCategory());
		existingBrandCategoryPojo.setBrand(brandCategoryPojo.getBrand());
		brandCategoryDao.update(existingBrandCategoryPojo);
	}

	private BrandCategoryPojo getCheck(Integer id) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(id);
		nullBrandCategoryPojoException(brandCategoryPojo);
		return brandCategoryPojo;
	}
	private void nullBrandCategoryPojoException(BrandCategoryPojo brandCategoryPojo) throws ApiException {
		if (brandCategoryPojo == null) {
			throw new ApiException("Brand Category does not exist");
		}
	}

	private void checkBrandCatDuplicateExists(String brand, String category) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = brandCategoryDao.select(brand,category);
		if(brandCategoryPojo!=null)
			throw new ApiException("Brand: " + brand + " in the category: " + category + " already exists.");
	}


	public List<BrandCategoryPojo> getAlikeBrandCategory(String brand, String category) throws ApiException {
		brand = StringUtil.toLowerCase(brand);
		category = StringUtil.toLowerCase(category);
		if(brand.isEmpty() && category.isEmpty()) {
			return getAllBrandCategory();
		} else if(category.isEmpty()) {
			return getBrandCategoryByBrand(brand);
		} else if(brand.isEmpty()) {
			return getBrandCategoryByCategory(category);
		} else {
			return Collections.singletonList(getByBrandCategory(brand, category));
		}
	}
}
