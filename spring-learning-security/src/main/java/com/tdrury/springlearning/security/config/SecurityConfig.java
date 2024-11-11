package com.tdrury.springlearning.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                   .authorizeHttpRequests(request ->
                      request.requestMatchers(new AntPathRequestMatcher("/private/**")).hasRole("USER"))
                   .authorizeHttpRequests(request ->
                      request.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                   .formLogin(Customizer.withDefaults())
                   .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.builder().username("user").password("{noop}password").roles("USER").build()
        );
    }
}
