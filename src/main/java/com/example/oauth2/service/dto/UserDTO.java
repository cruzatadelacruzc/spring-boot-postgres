package com.example.oauth2.service.dto;

import com.example.oauth2.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@ApiModel(value = "User transformation", description = "Data Transformation Object")
public class UserDTO {

    @ApiModelProperty(value = "Identifier, only update operation")
    private Long id;

    @NotNull
    @ApiModelProperty(example = "cmcruzata", value = "Username")
    private String username;

    @ApiModelProperty(example = "cmcruzata", value = "Password")
    private String password;

    @NotNull
    @ApiModelProperty(example = "Tu papa el inmortal", value = "Full name")
    private String fullname;

    @ApiModelProperty(example = "true", value = "Active user")
    private Boolean active;

    @NotNull
    @Email
    @ApiModelProperty(example = "cmcruzata@uci.cu", value = "Email")
    private String email;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.active = user.getActive();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public UserDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFullname() {
        return fullname;
    }

    public UserDTO setFullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public UserDTO setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }
}
