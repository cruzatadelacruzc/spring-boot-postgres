package com.example.oauth2.security;

import com.example.oauth2.domain.User;
import com.example.oauth2.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        log.debug("Authenticating using email: {}", email);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found in the database"));
        return UserPrincipal.create(user);
    }

    /**
     * Load user given Id user
     * @param id User Identifier
     * @return UserDetails
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        log.debug("Authenticating using Id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with Id " + id + " was not found in the database"));
        return UserPrincipal.create(user);
    }
}
