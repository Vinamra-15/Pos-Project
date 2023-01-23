package com.increff.pos.controller;

import java.util.List;

import com.increff.pos.dto.BrandCategoryDto;
import com.increff.pos.model.BrandCategoryData;
import com.increff.pos.model.BrandCategoryForm;
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
	public BrandCategoryData add(@RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
		return brandCategoryDto.add(brandCategoryForm);
	}


	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
	public BrandCategoryData get(@PathVariable Integer id) throws ApiException {
		return brandCategoryDto.get(id);
	}

	@ApiOperation(value = "Gets list of all brands")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public List<BrandCategoryData> getAll() {
		return brandCategoryDto.getAll();
	}

	@ApiOperation(value = "Updates a brand")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
	public BrandCategoryData update(@PathVariable Integer id, @RequestBody BrandCategoryForm brandCategoryForm) throws ApiException {
		return brandCategoryDto.update(id, brandCategoryForm);
	}
}
