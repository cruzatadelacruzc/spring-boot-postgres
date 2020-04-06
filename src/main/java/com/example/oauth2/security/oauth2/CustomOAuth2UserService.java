package com.example.oauth2.security.oauth2;

import com.example.oauth2.domain.AuthProvider;
import com.example.oauth2.domain.User;
import com.example.oauth2.repositories.UserRepository;
import com.example.oauth2.security.UserPrincipal;
import com.example.oauth2.security.oauth2.user.OAuth2UserInfo;
import com.example.oauth2.security.oauth2.user.OAuth2UserInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * It retrieves the details of the authenticated user and creates
 * a new entry in the database or updates the existing entry with the same email.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is called after an access token is obtained from the OAuth2 provider.
     * @param userRequest
     * @return OAuth2User
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        if (oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(oAuth2UserInfo.getEmail());
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!user.getProvider().equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            // If a user with the same email already exists in our database then we update his details
            user.setName(oAuth2UserInfo.getName());
            user.setImageUrl(oAuth2UserInfo.getImageUrl());
        } else {
            user = new User();
            user.setName(oAuth2UserInfo.getName());
            user.setEmail(oAuth2UserInfo.getEmail());
            user.setActive(true);
            user.setImageUrl(oAuth2UserInfo.getImageUrl());
            user.setProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
            user.setProviderId(userRequest.getClientRegistration().getRegistrationId());
        }
        userRepository.save(user);
        log.debug("Creating User {}", user);
        return UserPrincipal.create(user,oAuth2UserInfo.getAttributes());
    }
}
