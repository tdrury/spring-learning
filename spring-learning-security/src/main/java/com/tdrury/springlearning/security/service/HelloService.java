package com.tdrury.springlearning.security.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String hello(String name) {
        if (name != null) {
            return "Hello, "+name;
        } else {
            return "Hello";
        }
    }
}
