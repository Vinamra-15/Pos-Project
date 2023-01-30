package com.increff.pos.dto;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.SignUpForm;
import com.increff.pos.model.UserData;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.spring.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDto userDto;

    @Autowired
    private InfoData infoData;
    @Before
    public void addUser() throws ApiException {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail("xyz@increff.com");
        userPojo.setPassword("Pass1234");
        userService.add(userPojo);

    }
    @Test
    public void loginTest() throws ApiException {
        LoginForm loginForm = TestUtils.getLoginForm("xyz@increff.com","Pass1234");
        UserData userData = userDto.login(loginForm);
        assertEquals(loginForm.getEmail(),userData.getEmail());
    }
    @Test
    public void incorrectLoginTest() throws ApiException {
        LoginForm loginForm = TestUtils.getLoginForm("xyz@increff.com","Pass1234579");
        UserData userData = userDto.login(loginForm);
        assertNull(userData);
        assertEquals("Invalid username or password",infoData.getLoginMessage());
    }

    @Test
    public void signUpTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("abcd@increff.com","Pass1234","Pass1234");
        UserData userData = userDto.signUp(signUpForm);
        assertEquals("abcd@increff.com",userData.getEmail());
        assertEquals("",infoData.getSignUpMessage());
    }

    // user email already exists
    @Test
    public void alreadyExistsSignUpTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyz@increff.com","Pass1234","Pass1234");
        UserData userData = userDto.signUp(signUpForm);
        assertNull(userData);
        assertEquals("User with given email already exists",infoData.getSignUpMessage());
    }

    //Password and Confirm password do not match
    @Test
    public void invalidSignUpTest() throws ApiException {
        SignUpForm signUpForm = TestUtils.getSignUpForm("xyzasfd@increff.com","Pass1234","Pass12345");
        UserData userData = userDto.signUp(signUpForm);
        assertNull(userData);
        assertEquals("Passwords do not match!",infoData.getSignUpMessage());
    }
}
