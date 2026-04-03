package by.lobacevich.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getPrincipalId() {
        return ((UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .userId();
    }
}
