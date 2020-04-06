package com.example.oauth2.web.rest;

import com.example.oauth2.domain.User;
import com.example.oauth2.security.JWTFilter;
import com.example.oauth2.security.TokenProvider;
import com.example.oauth2.security.UserPrincipal;
import com.example.oauth2.service.UserService;
import com.example.oauth2.service.dto.UserDTO;
import com.example.oauth2.web.util.ResponseUtil;
import com.example.oauth2.web.vm.AccountVM;
import com.example.oauth2.web.vm.LoginVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to authenticate users.
 */
@Api(tags = {"Authenticate user"})
@RestController
@RequestMapping("/api")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    @Value("${spring.application.name}")
    private String applicationName;

    private final TokenProvider tokenProvider;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          UserService userService, TokenProvider tokenProvider) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @ApiOperation(value = "Login User")
    @PostMapping("/auth/login")
    public ResponseEntity<JWTToken> authorized(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getEmail(), loginVM.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + token);
        return new ResponseEntity<>(new JWTToken(token), httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "Get current user detail")
    @GetMapping("/auth/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtil.wrapOrNotFound(userService.getUser(userPrincipal.getId()));
    }

    /**
     * {@code POST  /users}  : Register an account.
     *
     * @param accountVM the user to create.
     * @return the {@link ResponseEntity} with status {@code 200 (ok)} and with body the email user
     */
    @PostMapping("/auth/account")
    @ApiOperation(value = "Register a account", response = UserDTO.class)
    public ResponseEntity<Map> createUser(@Valid @RequestBody AccountVM accountVM) {
        log.debug("REST request to register Account : {}", accountVM);
        Map<String, String> response = new HashMap<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setName(accountVM.getName());
        userDTO.setEmail(accountVM.getEmail());
        userDTO.setPassword(accountVM.getPassword());
        User newUser = userService.createUser(userDTO);
        response.put("username", newUser.getEmail());
        return ResponseEntity.ok(response);
    }


    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {
        private String accessToken;
        private String tokenType = "Bearer";

        public JWTToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public JWTToken setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public String getTokenType() {
            return tokenType;
        }

        public JWTToken setTokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
    }
}
