package com.github.greengerong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CSRFFilter implements Filter {

    private CSRFService csrfService;

    @Autowired
    public CSRFFilter(CSRFService csrfService) {
        this.csrfService = csrfService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        csrfService = new CSRFServiceImpl(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (csrfService != null && csrfService.isHandle(request)) {
            csrfService.handle(request, response);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        csrfService = null;
    }
}
