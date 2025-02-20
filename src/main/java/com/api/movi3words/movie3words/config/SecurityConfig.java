package com.api.movi3words.movie3words.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable() 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() 
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
