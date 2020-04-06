package com.example.oauth2.security.oauth2;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
