package com.example.oauth2.web.vm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public com.example.oauth2.web.vm.LoginVM setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public com.example.oauth2.web.vm.LoginVM setPassword(String password) {
        this.password = password;
        return this;
    }
}
