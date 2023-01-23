//package com.increff.pos.dto;
//
//import com.increff.pos.model.SignUpForm;
//import com.increff.pos.service.UserService;
//import com.increff.pos.spring.AbstractUnitTest;
//import com.increff.pos.util.TestUtils;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class UserDtoTest extends AbstractUnitTest {
//    @Autowired
//    private UserDto userDto;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    public void signUpTest(){
//        SignUpForm signUpForm = TestUtils.getSignUpForm("operator@increff.com","Pass1234","Pass1234");
//        boolean signedUp = userDto.signUp(signUpForm);
//
//    }
//}
