package com.tdrury.springlearning.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class SpringLearningSecurityApplication {

    @Autowired
    BearerTokenAuthenticationProvider bearerTokenAuthenticationProvider;

    public static void main(String[] args) {
        SpringApplication.run(SpringLearningSecurityApplication.class, args);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(bearerTokenAuthenticationProvider);
    }
}