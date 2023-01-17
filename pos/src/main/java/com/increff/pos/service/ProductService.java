package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

	@Autowired
	private ProductDao dao;

	public void add(ProductPojo p) throws ApiException {
		if(checkProductDuplicateExists(p.getBarcode())){
			throw new ApiException("Product with barcode: " + p.getBarcode()+ " already exists.");
		}
		dao.insert(p);
	}

	public ProductPojo get(Integer id) throws ApiException {
		return getCheck(id);
	}

	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}

	public ProductPojo getByBarcode(String barcode) throws ApiException{
		ProductPojo productPojo =  dao.selectByBarcode(barcode);
		if(productPojo==null)
		{
			throw new ApiException("No product exists with barcode " + barcode);
		}
		return productPojo;
	}


	public void update(Integer id, ProductPojo p) throws ApiException {
		if(checkProductDuplicateExists(id,p.getBarcode())){
			throw new ApiException("Product with barcode: " + p.getBarcode()+ " already exists.");
		}
		ProductPojo ex = getCheck(id);
		ex.setName(p.getName());
		ex.setBarcode(p.getBarcode());
		ex.setMrp(p.getMrp());
		ex.setBrandId(p.getBrandId());
		dao.update(ex);
	}
	public ProductPojo getCheck(Integer id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exist, id: " + id);
		}
		return p;
	}
	public boolean checkProductDuplicateExists(Integer id, String barcode){  //check for editing
		ProductPojo productPojo = dao.selectByBarcode(barcode);
		if(productPojo==null)
			return false;
		return id != productPojo.getId();
	}

	private boolean checkProductDuplicateExists(String barcode){ //check for adding
		ProductPojo productPojo = dao.selectByBarcode(barcode);
		if(productPojo!=null)
			return true;
		return false;
	}


}
