package com.tdrury.springlearning.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-path}")
public class BookController {

    @PostMapping("books")
    public BookResponse hello(BookRequest bookRequest) {
        log.info("bookRequest: {}", bookRequest);
        BookResponse bookResponse = new BookResponse();
        return bookResponse;
    }
}
