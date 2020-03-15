package com.example.oauth2.service.mapper;

import com.example.oauth2.domain.User;
import com.example.oauth2.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    default User fromId(Long id) {
        if( id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
