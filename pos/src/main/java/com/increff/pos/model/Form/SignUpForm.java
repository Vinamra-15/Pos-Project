package com.increff.pos.model.Form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
    private String email;
    private String password;
    private String confirmPassword;
}
