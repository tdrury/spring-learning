package com.tdrury.springlearning.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/public")
    public String index() {
        return "public page";
    }

    @GetMapping("/private")
    public String privatePage() {
        return "private page";
    }
}