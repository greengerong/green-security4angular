package com.github.greengerong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.github.greengerong.TokenFrom.Header;
import static com.github.greengerong.TokenFrom.Param;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CSRFInterceptor extends HandlerInterceptorAdapter {

    private final CSRFService csrfService;

    @Autowired
    public CSRFInterceptor(CSRFService csrfService) {
        this.csrfService = csrfService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (csrfService != null && csrfService.isHandle(request)) {
            csrfService.handle(request, response);
        }
        return true;
    }
}
