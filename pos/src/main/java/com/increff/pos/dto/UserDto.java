package com.increff.pos.dto;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.SignUpForm;
import com.increff.pos.model.UserData;
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
public class UserDto {

    @Autowired
    private UserService userService;
    @Autowired
    private InfoData info;

    public UserData login(LoginForm loginForm) throws ApiException {
        UserPojo userPojo = userService.get(loginForm.getEmail());
        boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
        if (!authenticated) {
            info.setMessage("Invalid username or password");
            return null;
        }
        return convert(userPojo);
    }

    public UserData signUp(SignUpForm signUpForm) throws ApiException {
        info.setMessage("");
        try
        {
            validateForm(signUpForm);
            UserPojo userPojo = convert(signUpForm);
            try{
                userService.add(userPojo);
                return convert(userPojo);
            }
            catch (ApiException e){
                info.setMessage(e.getMessage());
            }


        }
        catch (ApiException e){
            info.setMessage(e.getMessage());
        }

        return null;
    }


}
