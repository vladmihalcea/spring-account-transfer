package com.vladmihalcea.spring.util;

import javax.servlet.ServletRequest;

/**
 * The {@link UserRequestContext} stores the current client-specific timezone
 * that's captured during login.
 *
 * @author Vlad Mihalcea
 */
public class UserRequestContext {

    private static final ThreadLocal<UserRequest> userRequestHolder = new ThreadLocal<>();

    static UserRequest get() {
        return userRequestHolder.get();
    }

    public static String getIpAddress() {
        UserRequest userContext = get();
        return userContext != null ? userContext.getIpAddress() : null;
    }

    public static void set(ServletRequest request) {
        userRequestHolder.set(new UserRequest(request));
    }

    public static void reset() {
        userRequestHolder.remove();
    }

    public static class UserRequest {
        private final String ipAddress;

        public UserRequest(ServletRequest request) {
            this.ipAddress = request.getRemoteAddr();
        }

        public String getIpAddress() {
            return ipAddress;
        }
    }
}
