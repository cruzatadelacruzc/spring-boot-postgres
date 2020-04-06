package com.example.oauth2.security;

import com.example.oauth2.web.errors.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UnauthorizedRedirectUriException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public UnauthorizedRedirectUriException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public UnauthorizedRedirectUriException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.UNAUTHORIZED, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("message", "error."+ errorKey);
        parameters.put("param", entityName);
        return parameters;
    }
}
