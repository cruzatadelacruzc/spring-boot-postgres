package com.example.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties specific to Monolithic.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public class AppProperties {

    private final Security security = new Security();

    private final ClientApp clientApp = new ClientApp();

    private final CorsConfiguration cors = new CorsConfiguration();

    public AppProperties() {
    }

    public Security getSecurity() {
        return security;
    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public static class Security {
        private final OAuth2 oAuth2 = new OAuth2();

        private final Authentication authentication = new Authentication();

        public Security() {
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public OAuth2 getOAuth2() {
            return oAuth2;
        }

        public static class OAuth2 {
            private List<String> authorizedRedirectUris = new ArrayList<>();

            public OAuth2 setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
                this.authorizedRedirectUris = authorizedRedirectUris;
                return this;
            }

            public List<String> getAuthorizedRedirectUris() {
                return authorizedRedirectUris;
            }
        }

        public static class Authentication {
            private final Jwt jwt = new Jwt();

            public Authentication() {
            }

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {
                private String tokenSecret;
                private long tokenValidityInSeconds;

                public Jwt() {
                    this.tokenSecret = null;
                    this.tokenValidityInSeconds = 1800L;
                }

                public String getTokenSecret() {
                    return tokenSecret;
                }

                public Jwt setTokenSecret(String tokenSecret) {
                    this.tokenSecret = tokenSecret;
                    return this;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }
            }
        }
    }

    public static class ClientApp {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
