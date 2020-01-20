package com.tdrury.springlearning.rest.service;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloServiceTest {

    HelloService helloService = new HelloService();

    @Test
    public void whenSayHelloTim_thenReturnHelloTim() {
        // given
        String name = "Tim";

        // when
        String response = helloService.hello(name);

        // then
        assertThat(response, is("Hello, Tim"));
    }

    @Test
    public void whenSayWithNull_thenReturnHelloOnly() {
        // given
        String name = null;

        // when
        String response = helloService.hello(name);

        // then
        assertThat(response, is("Hello"));
    }

}
