package com.example.oauth2.web.rest;

import com.example.oauth2.domain.User;
import com.example.oauth2.service.UserService;
import com.example.oauth2.service.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(tags = {"User resource"})
@RestController
@RequestMapping(value = "/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final String ROLE_USER = "ROLE_USER";

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     *
     * @param userDto the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users")
    @ApiOperation(value = "Create a user", response = UserDTO.class)
    @PreAuthorize("hasRole(\"" + ROLE_ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDto) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDto);
        User newUser = userService.createUser(userDto);
        return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
                .body(newUser);
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDto the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     */
    @PutMapping("/users")
    @ApiOperation(value = "Update a user", response = UserDTO.class)
    @PreAuthorize("hasRole(\"" + ROLE_ADMIN + "\")")
    public ResponseEntity updateUser(@Valid @RequestBody UserDTO userDto) {
        log.debug("REST request to update User : {}", userDto);
        Optional<UserDTO> updatedUser = userService.updateUser(userDto);

        return updatedUser.map(ResponseEntity::ok).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param username the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user.
     */
    @ApiOperation(value = "Get a user by username", response = UserDTO.class)
    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole(\"" + ROLE_USER + "\")")
    public ResponseEntity getUser(@PathVariable String username) {
        log.debug("REST request to get User: {}", username);
        Optional<UserDTO> result = userService.getUserByUsername(username);
        return result.map(ResponseEntity::ok).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }


    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param username the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{username}")
    @ApiOperation(value = "Delete a user")
    @PreAuthorize("hasRole(\"" + ROLE_ADMIN + "\")")
    public ResponseEntity<Map> deleteUser(@PathVariable String username) {
        log.debug("REST request to delete User: {}", username);
        Map<String, Boolean> response = new HashMap<>();
        boolean result = userService.deleteUser(username);
        response.put("isDelete", result);
        return ResponseEntity.ok(response);
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @ApiOperation(value = "List all Users")
    @GetMapping("/users")
    @PreAuthorize("hasRole(\"" + ROLE_USER + "\")")
    public ResponseEntity<List<UserDTO>> getAllUser(Pageable pageable) {
        log.debug("REST request to get a page of Users");
        final Page<UserDTO> page = userService.getAllUsers(pageable);
        return ResponseEntity.ok()
                .body(page.getContent());
    }
}
