package com.github.greengerong.csrf;

import javax.servlet.FilterConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


public class DefaultCSRFService implements CSRFService {

    private static final String[] EXCLUDE_METHODS = {"GET"};
    public static final String XSRF_TOKEN_HEADER_KEY = "X-XSRF-TOKEN";
    public static final String XSRF_TOKEN_SESSION_KEY = "XSRF-TOKEN";
    public static final String XSRF_TOKEN_COOKIE_KEY = "XSRF-TOKEN";
    private FilterConfig filterConfig;

    public DefaultCSRFService(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    private boolean isHandle(HttpServletRequest request) {
        return !Arrays.asList(EXCLUDE_METHODS).contains(request.getMethod().toUpperCase());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {

        final boolean shouldHandle = isHandle(request);
        final Object orginToken = request.getSession(true).getAttribute(XSRF_TOKEN_SESSION_KEY);
        if (orginToken == null && !shouldHandle) {
            responseNewToken(request, response);
            return;
        }

        if (shouldHandle) {
            if (isValidXSRFToken(request, orginToken)) {
                responseNewToken(request, response);
                return;
            }

            try {
                response.sendError(403, "XSRF token is not valid.");
            } catch (IOException e) {
                throw new XSRFRuntimeException(e);
            }
        }
    }


    private void responseNewToken(HttpServletRequest request, HttpServletResponse response) {
        final String token = UUID.randomUUID().toString();
        request.getSession(true).setAttribute(XSRF_TOKEN_SESSION_KEY, token);
        final Cookie tokenCookie = new Cookie(XSRF_TOKEN_COOKIE_KEY, token);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }

    private boolean isValidXSRFToken(HttpServletRequest request, Object orginToken) {
        return orginToken != null && orginToken.equals(request.getHeader(XSRF_TOKEN_HEADER_KEY));
    }
}
