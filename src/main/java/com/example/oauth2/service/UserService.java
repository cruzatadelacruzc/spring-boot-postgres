package com.example.oauth2.service;

import com.example.oauth2.domain.AuthProvider;
import com.example.oauth2.domain.User;
import com.example.oauth2.repositories.UserRepository;
import com.example.oauth2.service.dto.UserDTO;
import com.example.oauth2.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Create a new User
     *
     * @param userDTO Data container {@link UserDTO}
     * @return User
     */
    public User createUser(UserDTO userDTO) {
        if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent()){
            throw new EmailAlreadyUsedException();
        }
        User user1 = userMapper.toEntity(userDTO);
        user1.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user1.setProvider(AuthProvider.local);
        userRepository.save(user1);
        log.debug("Created Information for User: {}", user1);
        return user1;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setActive(userDTO.getActive());
                    user.setEmail(userDTO.getEmail());
                    user.setName(userDTO.getName());
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(UserDTO::new);
    }

    /**
     * Get all Users
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    /**
     * Get a User
     * @param id Identifier of the user
     * @return UserDTO if exists
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUser(Long id){
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /**
     * Get a User given a username
     * @param email Email of the user
     * @return UserDTO if exists
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email){
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    /**
     * Delete a User given login
     *
     * @param email Email of the user
     */
    public boolean deleteUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            log.debug("Deleted User: {}", user);
            return true;
        }
        return false;
    }


    /**
     * Find all Users given login
     *
     */
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Find All Users: {}", users);
        return userMapper.toDto(users);
    }
}
