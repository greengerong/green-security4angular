package com.github.greengerong.csrf;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CSRFFilter implements Filter {

    private CSRFService csrfService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        csrfService = new DefaultCSRFService(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        csrfService.handle(request, response);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        csrfService = null;
    }
}
