package com.tdrury.springlearning.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class HelloService {

    @PreAuthorize("USER")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("hello: authentication={}", authentication);
        Optional<? extends GrantedAuthority> roleAdmin = authentication.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findAny();
        if (roleAdmin.isPresent()) {
            return "Hello Admin, " + authentication.getName();
        } else {
            return "Hello, " + authentication.getName();
        }
    }

}
