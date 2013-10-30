package com.github.greengerong.xss;


import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class XSSFilter implements Filter {
    public static final String DEFAULT_XSS_PREFIXED = ")]}`,\\n";
    private static final String[] contentTypes = new String[]{"application/json",
            "application/x-javascript", "text/javascript", "text/x-javascript", "text/x-json"};
    private String prefixed;
    private boolean enableXSS;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final String prefixed = filterConfig.getInitParameter("prefixed");
        final String enableXSS = filterConfig.getInitParameter("enableXSS");
        this.prefixed = StringUtils.isNotBlank(prefixed) ? prefixed : DEFAULT_XSS_PREFIXED;
        this.enableXSS = StringUtils.isNotBlank(enableXSS) ? Boolean.valueOf(enableXSS) : true;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (enableXSS) {
            handleRequest(servletRequest, servletResponse);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleRequest(ServletRequest request, ServletResponse response) throws IOException {
        if (isJsonRequest(request)) {
            final PrintWriter writer = response.getWriter();
            writer.write(prefixed);
            writer.flush();
        }
    }

    private boolean isJsonRequest(ServletRequest request) {
        final String contentType = request.getContentType();
        return true && StringUtils.isNotBlank(contentType) && Arrays.asList(contentTypes).contains(contentType.toLowerCase());
    }

    @Override
    public void destroy() {

    }
}
