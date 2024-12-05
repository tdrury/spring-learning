package com.tdrury.springlearning.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "public page";
    }

    @GetMapping("/private")
    public String privatePage() {
        return "private page";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}