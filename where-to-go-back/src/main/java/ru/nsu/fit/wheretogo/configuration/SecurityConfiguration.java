package ru.nsu.fit.wheretogo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.nsu.fit.wheretogo.service.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthService authService;

    public SecurityConfiguration(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .userDetailsService(authService)
                .sessionManagement().disable();

        return http.build();
    }
}
