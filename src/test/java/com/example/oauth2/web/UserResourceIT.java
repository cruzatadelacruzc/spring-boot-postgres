package com.example.oauth2.web;

import com.example.oauth2.Oauth2Application;
import com.example.oauth2.domain.User;
import com.example.oauth2.service.UserService;
import com.example.oauth2.service.dto.UserDTO;
import com.example.oauth2.service.mapper.UserMapper;
import com.example.oauth2.web.errors.ExceptionTranslator;
import com.example.oauth2.web.rest.UserResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Oauth2Application.class)
public class UserResourceIT {
    private static final String DEFAULT_USERNAME = "lucas";
    private static final String UPDATED_USERNAME = "guido";

    private static final String DEFAULT_PASSWORD = "passlucas";
    private static final String UPDATED_PASSWORD = "passguido";

    private static final String DEFAULT_FULLAME = "Lucas";
    private static final String UPDATED_FULLNAME = "Guido";

    private static final String DEFAULT_EMAIL = "lucas@gmail.com";
    private static final String UPDATED_EMAIL = "guido@gmail.com";

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private User user;

    private MockMvc restUserMockMvc;

    @BeforeEach
    public void setUp() {
        UserResource userResource = new UserResource(userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @BeforeEach
    public void init() {
        this.user = new User();
        user.setUsername(DEFAULT_USERNAME);
        user.setEmail(DEFAULT_EMAIL);
        user.setFullname(DEFAULT_FULLAME);
        user.setActive(true);
        user.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void testCreateUser() throws Exception {
        int initialDatabaseSize = userService.findAllUsers().size();
        restUserMockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(this.user)))
                .andExpect(status().isCreated());

        List<UserDTO> userDTOList = userService.findAllUsers();
        assertThat(initialDatabaseSize).isEqualTo(userDTOList.size() - 1);
        UserDTO userDTO = userDTOList.get(userDTOList.size() - 1);
        assertThat(userDTO.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.getFullname()).isEqualTo(DEFAULT_FULLAME);
    }

    @Test
    public void testCreateUserWithNullUsername() throws Exception {
        user.setUsername(null);
        restUserMockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(this.user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithWrongEmail() throws Exception {
        user.setEmail("paolo.com");
        restUserMockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(this.user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void testGetUser() throws Exception {
        User userStored =  userService.createUser(userMapper.toDto(user));
        restUserMockMvc.perform(get("/api/users/{username}", userStored.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userStored.getUsername()))
                .andExpect(jsonPath("$.email").value(userStored.getEmail()))
                .andExpect(jsonPath("$.fullname").value(userStored.getFullname()))
                .andExpect(jsonPath("$.active").value(userStored.getActive()));
    }

    @Test
    public void testNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testGetAllUsers() throws Exception {
        User userStored = userService.createUser(userMapper.toDto(user));
        restUserMockMvc.perform(get("/api/users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].username").value(userStored.getUsername()))
                .andExpect(jsonPath("$.[*].email").value(userStored.getEmail()))
                .andExpect(jsonPath("$.[*].fullname").value(userStored.getFullname()))
                .andExpect(jsonPath("$.[*].active").value(userStored.getActive()));
    }

    @Test
    @Transactional
    public void testDeleteUser() throws Exception {
        User userStored = userService.createUser(userMapper.toDto(user));
        int initialDatabaseSize = userService.findAllUsers().size();
        restUserMockMvc.perform(delete("/api/users/{username}", userStored.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isDelete").value(true));
        List<UserDTO> userDTOList = userService.findAllUsers();
        assertThat(userDTOList.size()).isEqualTo(initialDatabaseSize -1);
    }

    @Test
    @Transactional
    public void testUpdateUser() throws Exception {
       User userStored =  userService.createUser(userMapper.toDto(user));

        int initialDatabaseSize = userService.findAllUsers().size();

        Optional<UserDTO> userToUpdate = userService.getUser(userStored.getId());

        assertThat(userToUpdate.isPresent()).isTrue();

        userToUpdate.get().setPassword(UPDATED_PASSWORD);
        userToUpdate.get().setUsername(UPDATED_USERNAME);
        userToUpdate.get().setFullname(UPDATED_FULLNAME);
        userToUpdate.get().setEmail(UPDATED_EMAIL);
        userToUpdate.get().setActive(false);

        restUserMockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(userToUpdate.get())))
                .andExpect(status().isOk());

        List<UserDTO> userDTOList = userService.findAllUsers();
        assertThat(initialDatabaseSize).isEqualTo(userDTOList.size());
        UserDTO userDTO = userDTOList.get(userDTOList.size() - 1);

        assertThat(userDTO.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(userDTO.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(userDTO.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(userDTO.getActive()).isEqualTo(false);
    }
}
