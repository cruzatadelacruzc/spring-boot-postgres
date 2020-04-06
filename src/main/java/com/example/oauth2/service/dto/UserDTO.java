package com.example.oauth2.service.dto;

import com.example.oauth2.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@ApiModel(value = "User transformation", description = "Data Transformation Object")
public class UserDTO {

    @ApiModelProperty(value = "Identifier, only update operation")
    private Long id;

    @ApiModelProperty(example = "cmcruzata", value = "Password")
    private String password;

    @NotBlank
    @ApiModelProperty(example = "Tu papa el inmortal", value = "Full name")
    private String name;

    @ApiModelProperty(example = "true", value = "Active user")
    private Boolean active;

    @NotBlank
    @Email
    @ApiModelProperty(example = "cmcruzata@uci.cu", value = "Email")
    private String email;

    @ApiModelProperty(value = "Image url")
    private String imageUrl;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
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

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDTO setName(String name) {
        this.name = name;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public UserDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        if (userDTO.id == null || getId() == null) return false;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
