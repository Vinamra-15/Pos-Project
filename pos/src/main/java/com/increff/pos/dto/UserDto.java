package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.SignUpForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.increff.pos.util.ConvertUtil.convert;
import static com.increff.pos.util.Validate.validateForm;

@Component
@PropertySource(value="file:./pos.properties")
public class UserDto {

    @Autowired
    private UserService userService;
    @Autowired
    private InfoData info;
    @Value("${admin.email}")
    private String adminEmail;
    public boolean login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userService.get(loginForm.getEmail());
        boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return false;
        }
        authenticate(req,userPojo);
        return true;
    }

    private void authenticate(HttpServletRequest req, UserPojo userPojo){
        // Create authentication object
        Authentication authentication = convert(userPojo,adminEmail);
        // Create new session
        HttpSession session = req.getSession(true);
        // Attach Spring SecurityContext to this new session
        SecurityUtil.createContext(session);
        // Attach Authentication object to the Security Context
        SecurityUtil.setAuthentication(authentication);
    }

    public boolean signUp(HttpServletRequest req, SignUpForm signUpForm) throws ApiException {
        validateForm(signUpForm);
        UserPojo userPojo = convert(signUpForm);
        userService.add(userPojo);
        authenticate(req,userPojo);
        return true;
    }
}
