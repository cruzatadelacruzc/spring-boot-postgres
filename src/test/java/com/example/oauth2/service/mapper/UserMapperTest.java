package com.example.oauth2.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(userMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userMapper.fromId(null)).isNull();
    }

}
