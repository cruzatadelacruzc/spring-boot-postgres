package com.example.oauth2.security.oauth2;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    /**
     * Get cookie given a name
     *
     * @param httpServletRequest
     * @param name               cookie name
     * @return Optional.of(cookie)
     */
    public static Optional<Cookie> getCookie(HttpServletRequest httpServletRequest, String name) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Create a new cookie
     *
     * @param httpServletResponse
     * @param name                cookie name
     * @param value               cookie value
     * @param maxAge              time life
     */
    public static void addCookie(HttpServletResponse httpServletResponse, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * Delete cookie if exists
     *
     * @param response
     * @param request
     * @param name     cookie name
     */
    public static void deleteCookie(HttpServletResponse response, HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object o) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(o));
    }

    public static <T> T deserialization(Cookie cookie, Class<T> clazz){
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
