package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

	private Integer id;
	private String barcode;
	private String name;
	private Double mrp;
	private String brand;
	private String category;
}
