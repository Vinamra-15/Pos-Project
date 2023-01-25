package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(path = "/site/signup", method = RequestMethod.GET)
	public ModelAndView showSignUpPage() {
		infoData.setSignUpMessage("");
		return mav("signup.html");
	}

	@RequestMapping(path = "/site/login", method = RequestMethod.GET)
	public ModelAndView showLoginPage() {
		infoData.setLoginMessage("");
		return mav("login.html");
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

}
