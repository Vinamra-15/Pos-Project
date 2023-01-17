package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteUiController extends AbstractUiController {

	// WEBSITE PAGES
	@Autowired
	private InfoData infoData;
	@RequestMapping(value = "")
	public ModelAndView index() {
		return new ModelAndView("redirect:/site/login");
	}

	@RequestMapping(value = "/site/login")
	public ModelAndView login() {
		if(!infoData.getEmail().equals("")){
			return new ModelAndView("redirect:/ui/home");
		}
		return mav("login.html");
	}

	@RequestMapping(value = "/site/signup")
	public ModelAndView signup() {
		if(!infoData.getEmail().equals("")){
			return new ModelAndView("redirect:/ui/home");
		}
		return mav("signup.html");
	}

	@RequestMapping(value = "/site/logout")
	public ModelAndView logout() {
		return mav("logout.html");
	}

//	@RequestMapping(value = "/site/pricing")
//	public ModelAndView pricing() {
//		return mav("pricing.html");
//	}
//
//	@RequestMapping(value = "/site/features")
//	public ModelAndView features() {
//		return mav("features.html");
//	}

}
