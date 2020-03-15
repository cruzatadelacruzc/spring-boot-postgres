package com.example.oauth2.service.dto;

import com.example.oauth2.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserDTO {

    private Long id;

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String fullname;

    private Boolean active;

    @NotNull
    @Email
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
