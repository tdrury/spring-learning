package com.tdrury.springlearning.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    final CustomOAuth2UserService oauthUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
                request.requestMatchers("/", "/login").permitAll();
                request.anyRequest().authenticated();
            })
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2Login(oauth2login -> {
                oauth2login
                    .loginPage("/login")
                    .userInfoEndpoint(config -> config.userService(oauthUserService))
                    .successHandler((request, response, authentication)
                                        -> response.sendRedirect("/profile"));
            });
        return http.build();
    }
}
