package com.tdrury.springlearning.security.controller;

import com.tdrury.springlearning.security.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${spring.data.rest.base-path}")
public class HelloController {

    private final HelloService helloService;

    @GetMapping("hello")
    public String hello() {
        return helloService.hello();
    }
}
