package com.example.oauth2.security;

import com.example.oauth2.config.AppProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This class contains code to generate and verify Json Web Tokens
 */
@Component
public class TokenProvider implements InitializingBean {

    private final CustomUserDetailsService customUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;


    private long tokenValidityInMilliseconds;

    public TokenProvider(CustomUserDetailsService customUserDetailsService, AppProperties appProperties) {
        this.customUserDetailsService = customUserDetailsService;
        this.appProperties = appProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.tokenValidityInMilliseconds = 1000 * appProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        long now = (new Date()).getTime();
        Date expiryDate = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getSecurity().getAuthentication().getJwt().getTokenSecret())
                .compact();
    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getSecurity().getAuthentication().getJwt().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = customUserDetailsService.loadUserById(Long.parseLong(claims.getSubject()));
       return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getSecurity().getAuthentication().getJwt().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties
                        .getSecurity()
                        .getAuthentication()
                        .getJwt()
                        .getTokenSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
