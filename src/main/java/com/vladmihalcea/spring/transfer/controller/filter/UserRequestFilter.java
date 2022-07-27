package com.vladmihalcea.spring.transfer.controller.filter;

import com.vladmihalcea.spring.util.UserRequestContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * The {@link UserRequestFilter} captures the client-specific info provided by the user.
 *
 * @author Vlad Mihalcea
 */
@Component
@Order(1)
public class UserRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            UserRequestContext.set(request);
            chain.doFilter(request, response);
        } finally {
            UserRequestContext.reset();
        }
    }
}
