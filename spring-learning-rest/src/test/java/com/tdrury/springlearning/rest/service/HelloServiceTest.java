package com.tdrury.springlearning.rest.service;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class HelloServiceTest {

    HelloService helloService = new HelloService();

    @Test
    void whenSayHelloTim_thenReturnHelloTim() {
        // given
        String name = "Tim";

        // when
        String response = helloService.hello(name);

        // then
        assertThat(response, is("Hello, Tim"));
    }

    @Test
    void whenSayWithNull_thenReturnHelloOnly() {
        // when
        String response = helloService.hello(null);

        // then
        assertThat(response, is("Hello"));
    }

}
