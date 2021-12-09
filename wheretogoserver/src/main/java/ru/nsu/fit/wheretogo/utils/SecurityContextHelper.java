package ru.nsu.fit.wheretogo.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelper {
    public static String email()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public static void setNotAuthenticated()
    {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private SecurityContextHelper(){}
}
