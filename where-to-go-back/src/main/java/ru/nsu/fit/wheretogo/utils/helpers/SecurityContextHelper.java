package ru.nsu.fit.wheretogo.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityContextHelper {
    public static String email() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static void setNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
