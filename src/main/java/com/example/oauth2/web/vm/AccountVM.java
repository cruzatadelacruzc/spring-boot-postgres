package com.example.oauth2.web.vm;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AccountVM {

    @Size(min = 3)
    @ApiModelProperty(example = "cmcruzata", value = "Password")
    private String password;

    @NotBlank
    @ApiModelProperty(example = "Tu papa el inmortal", value = "Full name")
    private String name;

    @NotBlank
    @Email
    @ApiModelProperty(example = "cmcruzata@uci.cu", value = "Email")
    private String email;

    public String getPassword() {
        return password;
    }

    public AccountVM setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountVM setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountVM setEmail(String email) {
        this.email = email;
        return this;
    }
}
