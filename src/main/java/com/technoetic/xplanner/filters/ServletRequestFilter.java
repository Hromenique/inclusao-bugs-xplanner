package com.technoetic.xplanner.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter saves the HTTP request for SOAP purposes. The SOAP adapters don't have
 * access to the HTTP execution context.
 */
public class ServletRequestFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                            FilterChain filterChain) throws IOException, ServletException {
        try {
            ThreadServletRequest.set((HttpServletRequest)request);
            filterChain.doFilter(request, response);
        } finally {
            ThreadServletRequest.set(null);
        }
    }

    public void destroy() {
    }

}
