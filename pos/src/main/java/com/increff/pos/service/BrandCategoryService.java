package com.increff.pos.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandCategoryDao;
import com.increff.pos.pojo.BrandCategoryPojo;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BrandCategoryService {

	@Autowired
	private BrandCategoryDao dao;


	public void add(BrandCategoryPojo p) throws ApiException {
		if(checkBrandCatDuplicateExists(p.getBrand(),p.getCategory())){
			throw new ApiException("Brand: " + p.getBrand() + " in the category: " + p.getCategory() + " already exists.");
		}
		dao.insert(p);
	}

	public BrandCategoryPojo get(Integer id) throws ApiException {
		return getCheck(id);
	}
	public List<BrandCategoryPojo> getAll() {
		return dao.selectAll();
	}

	public List<BrandCategoryPojo> getByBrand(String brand) {
		return dao.selectByBrand(brand);
	}

	public List<BrandCategoryPojo> getByCategory(String category) {
		return dao.selectByCategory(category);
	}

	public BrandCategoryPojo getByBrandCategory(String brand,String category) throws ApiException {
		BrandCategoryPojo brandCategoryPojo = dao.select(brand,category);
		if(brandCategoryPojo==null)
			throw new ApiException("Brand: "+brand+" in category: "+category+" does not exist");
		return brandCategoryPojo;
	}

	public void update(Integer id, BrandCategoryPojo p) throws ApiException {
		if(checkBrandCatDuplicateExists(p.getBrand(),p.getCategory())){
			throw new ApiException("Brand: " + p.getBrand() + " in the category: " + p.getCategory() + " already exists.");
		}
		BrandCategoryPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}

	private BrandCategoryPojo getCheck(Integer id) throws ApiException {
		BrandCategoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exist, id: " + id);
		}
		return p;
	}

	public BrandCategoryPojo getCheckWithBrandCategory(String brand, String category) throws ApiException{
		BrandCategoryPojo p = dao.select(brand,category);
		if (p == null) {
			throw new ApiException("Brand with brand: " + brand + " and category: " + category+" does not exist!");
		}
		return p;
	}

	private boolean checkBrandCatDuplicateExists(String brand, String category){

		BrandCategoryPojo brandCategoryPojo = dao.select(brand,category);
		if(brandCategoryPojo==null)
			return false;
		return true;
	}


}
