package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.increff.pos.util.Normalize.normalizePojo;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public void addProduct(ProductPojo productPojo) throws ApiException {
		normalizePojo(productPojo);
		checkProductDuplicateExists(productPojo.getBarcode());
		productDao.insert(productPojo);
	}

	public ProductPojo getProduct(Integer id) throws ApiException {
		return getCheck(id);
	}

	public List<ProductPojo> getAllProducts() {
		return productDao.selectAll();
	}

	public ProductPojo getProductByBarcode(String barcode) throws ApiException{
		barcode = StringUtil.toLowerCase(barcode);
		ProductPojo productPojo =  productDao.selectByBarcode(barcode);
		if(productPojo==null)
		{
			throw new ApiException("No product exists with barcode " + barcode);
		}
		return productPojo;
	}
	//get product from id,if product barcode is same then  do changes if not check the barcode exist or not if exist
	//  throw barcode exist already using checkProductDuplicateExists method else update barcode

	public void updateProduct(Integer id, ProductPojo productPojo) throws ApiException {
		normalizePojo(productPojo);
		ProductPojo existing = getCheck(id);
		if(!existing.getBarcode().equals(productPojo.getBarcode())){
			checkProductDuplicateExists(productPojo.getBarcode());
			existing.setBarcode(productPojo.getBarcode());
		}
		existing.setName(productPojo.getName());
		existing.setMrp(productPojo.getMrp());
		existing.setBrandId(productPojo.getBrandId());
		productDao.update(existing);

	}
	private ProductPojo getCheck(Integer id) throws ApiException {
		ProductPojo productPojo = productDao.select(id);
		if (productPojo == null) {
			throw new ApiException("Product with given ID does not exist, id: " + id);
		}
		return productPojo;
	}
	private void checkProductDuplicateExists(String barcode) throws ApiException {
		barcode = StringUtil.toLowerCase(barcode);
		ProductPojo productPojo = productDao.selectByBarcode(barcode);
		if(productPojo!=null)
			throw new ApiException("Product with barcode: " + barcode+ " already exists.");
	}

    public ProductPojo getCheckSellingPrice(String barcode, Double sellingPrice) throws ApiException {
		ProductPojo productPojo = getProductByBarcode(barcode);
		if(sellingPrice>productPojo.getMrp()){
			throw new ApiException("Selling Price cannot be greater than MRP for barcode: " + productPojo.getBarcode() +  ", Mrp: " + productPojo.getMrp());
		}
		return productPojo;
    }
}
