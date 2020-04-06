package com.example.oauth2.web.errors;

import java.net.URI;

public final class ErrorConstants {
    public static final String ERR_VALIDATION = "error.validation";
    private static final String PROBLEM_BASE_URL = "https://localhost:8080/problem"; //URL_CLIENT_FOR_DISPLAY_PROBLEM
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");

}
