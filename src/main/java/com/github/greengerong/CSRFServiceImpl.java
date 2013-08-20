package com.github.greengerong;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Pattern;

import static com.github.greengerong.TokenFrom.Header;
import static com.github.greengerong.TokenFrom.Param;
import static com.google.common.collect.FluentIterable.from;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CSRFServiceImpl implements CSRFService {

    private static final String TOKEN_PARAMETER_NAME = "token.parameter.name";
    private static final List<String> METHODS_TO_CHECK = unmodifiableList(Arrays.asList("POST", "PUT", "DELETE"));
    private static final String TOKEN_FROM = "token.from";
    private static final String INVALID_TOKEN_KEY = "invalid.token.key";
    private static final String URL_IGNORE = "url.ignore";

    private String tokenParameterName = "csrf_token";
    private TokenFrom tokenFrom = Param;
    private String invalidCsrfTokenParameter = "invalid_csrf_token";
    private List<String> ignorePatterns = Lists.newArrayList();

    public CSRFServiceImpl(Properties properties) {
        LoadFrom(properties);
    }

    private void LoadFrom(Properties properties) {
        tokenParameterName = tryGetValue(properties, TOKEN_PARAMETER_NAME, tokenParameterName);
        invalidCsrfTokenParameter = tryGetValue(properties, INVALID_TOKEN_KEY, invalidCsrfTokenParameter);
        tokenFrom = TokenFrom.valueOf(tryGetValue(properties, TOKEN_FROM, tokenFrom.name()));
        ignorePatterns = Arrays.asList(tryGetValue(properties, URL_IGNORE, "").split(","));
    }

    private static String tryGetValue(Properties properties, String key, String defaultValue) {
        if (properties.containsKey(TOKEN_PARAMETER_NAME)) {
            return properties.getProperty(key);
        }
        return defaultValue;
    }


    public String generateCSRFToken() {
        String token = UUID.randomUUID().toString();
        return (new BASE64Encoder()).encode(token.getBytes());
    }

    @Override
    public boolean isHandle(HttpServletRequest request) {
        return METHODS_TO_CHECK.contains(defaultIfBlank(request.getMethod(), "").toUpperCase());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        if (!ignored(request) && !accepted(request)) {
            response.addHeader(invalidCsrfTokenParameter, Boolean.toString(true));
            throw new RuntimeException("CSRF token is not right.");
        }

        refreshToken(request, response);
    }

    private boolean ignored(HttpServletRequest request) {
        final String url = request.getRequestURI();
        return from(ignorePatterns).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String pattern) {
                return Pattern.compile(pattern).matcher(url).find();
            }
        });
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String token = generateCSRFToken();
        request.getSession(true).setAttribute(TOKEN_ATTRIBUTE_NAME, token);
        responseToken(response, token);
    }

    private void responseToken(HttpServletResponse response, String token) {
        if (Header.include(tokenFrom)) {
            response.addHeader(tokenParameterName, token);
        }
    }

    public boolean accepted(HttpServletRequest request) {
        final String requestToken = getRequestToken(request);
        final HttpSession session = request.getSession(false);
        if (session != null) {
            final String lastToken = (String) session.getAttribute(TOKEN_ATTRIBUTE_NAME);
            if (isNotBlank(lastToken) && !lastToken.equalsIgnoreCase(requestToken)) {
                return false;
            }
        }

        return true;
    }

    private String getRequestToken(HttpServletRequest request) {
        String token = "";
        final String parameter = request.getParameter(tokenParameterName);
        final String header = request.getHeader(tokenParameterName);
        if (Param.include(tokenFrom) && isNotBlank(parameter)) {
            token = parameter;
        } else if (Header.include(tokenFrom) && isNotBlank(header)) {
            token = header;
        }

        return token;
    }
}
