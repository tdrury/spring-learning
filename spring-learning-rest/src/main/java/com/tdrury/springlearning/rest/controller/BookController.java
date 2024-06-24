package com.tdrury.springlearning.rest.controller;

import com.tdrury.springlearning.rest.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-path}")
public class BookController {

//    @Autowired
//    private HelloService helloService;

    @PostMapping("books")
    public BookResponse hello(BookRequest bookRequest) {
        log.info("bookRequest: {}", bookRequest);
        BookResponse bookResponse = new BookResponse();
        return bookResponse;
    }
}
