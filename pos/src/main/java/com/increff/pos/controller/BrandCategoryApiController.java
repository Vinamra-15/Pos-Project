package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.BrandCategoryDto;
import com.increff.pos.model.Data.BrandCategoryData;
import com.increff.pos.model.Form.BrandCategoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandCategoryApiController {

	@Autowired
	private BrandCategoryDto brandCategoryDto;

	@ApiOperation(value = "Adds a brand")
	@RequestMapping(path = "/api/brands", method = RequestMethod.POST)
	public BrandCategoryData addBrandCategory(@RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
		return brandCategoryDto.addBrandCategory(brandCategoryForm);
	}


	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
	public BrandCategoryData getBrandCategory(@PathVariable Integer id) throws ApiException {
		return brandCategoryDto.getBrandCategory(id);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public List<BrandCategoryData> getAllBrandCategory() {
		return brandCategoryDto.getAllBrandCategory();
	}

	@ApiOperation(value = "Updates a brand")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
	public BrandCategoryData updateBrandCategory(@PathVariable Integer id, @RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
		return brandCategoryDto.update(id, brandCategoryForm);
	}
}
