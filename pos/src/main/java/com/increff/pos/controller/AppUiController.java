package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {
	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brands")
	public ModelAndView brandCategory() {
		return mav("brand-category.html");
	}

	@RequestMapping(value = "/ui/products")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("order.html");
	}


	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}

	@RequestMapping(value = "/ui/reports")
	public ModelAndView reports() {
		return mav("reports.html");
	}

	@RequestMapping(value = "/ui/reports/inventory")
	public ModelAndView inventoryReport() {
		return mav("inventory-report.html");
	}

	@RequestMapping(value = "/ui/reports/brands")
	public ModelAndView brandReport() {
		return mav("brand-report.html");
	}

	@RequestMapping(value = "/ui/reports/sales")
	public ModelAndView salesReport() {
		return mav("sales-report.html");
	}

	@RequestMapping(value = "/ui/reports/day-sales")
	public ModelAndView daySalesReport() {
		return mav("day-sales-report.html");
	}

}
