package com.increff.pos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.SignUpForm;
import com.increff.pos.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.LoginForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.SecurityUtil;

import io.swagger.annotations.ApiOperation;

import static com.increff.pos.util.ConvertUtil.convert;

@Controller
public class LoginSignUpController {
	@Autowired
	private UserDto userDto;
	@Value("${admin.email}")
	private String adminEmail;

	@Autowired
	private InfoData info;

	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/site/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
		UserData authenticatedUser = userDto.login(loginForm);
		if (authenticatedUser==null) {
			ModelAndView mav = new ModelAndView("login.html");
			mav.addObject("info", info);
			return mav;
		}
		authenticate(req,authenticatedUser);
		return new ModelAndView("redirect:/ui/home");
	}

	@ApiOperation(value = "Signs up a user")
	@RequestMapping(path = "/site/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView signUp(HttpServletRequest req, SignUpForm signUpForm) throws ApiException {
		UserData signedUpUserData = userDto.signUp(signUpForm);
		if(signedUpUserData==null){
			ModelAndView mav = new ModelAndView("signup.html");
			mav.addObject("info", info);
			return mav;
		}
		authenticate(req,signedUpUserData);
		return new ModelAndView("redirect:/ui/home");
	}

	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/login");
	}

	private void authenticate(HttpServletRequest req, UserData userData){
		Authentication authentication = convert(userData,adminEmail);
		HttpSession session = req.getSession(true);
		SecurityUtil.createContext(session);
		SecurityUtil.setAuthentication(authentication);
	}


}
