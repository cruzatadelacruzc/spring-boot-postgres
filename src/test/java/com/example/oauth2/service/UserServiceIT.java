package com.example.oauth2.service;

import com.example.oauth2.Oauth2Application;
import com.example.oauth2.domain.AuthProvider;
import com.example.oauth2.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link UserService}.
 */
@SpringBootTest(classes = Oauth2Application.class)
@Transactional
public class UserServiceIT {


    private static final String DEFAULT_FULLNAME = "Lucas";

    private static final String DEFAULT_EMAIL = "lucas@gmail.com";

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setName(DEFAULT_FULLNAME);
        user.setProvider(AuthProvider.local);
        user.setPassword(RandomStringUtils.random(60));
    }
}
