package com.tdrury.springlearning.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-path}")
public class BookController {

    @PostMapping(value = "books", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookResponse books(BookRequest bookRequest) {
        log.info("books: bookRequest: {}", bookRequest);
        BookResponse bookResponse = new BookResponse();
        log.info("books: bookResponse={}", bookResponse);
        return bookResponse;
    }
}
