package com.tdrury.springlearning.hexagonal.adapters.authorclient;

import com.tdrury.springlearning.hexagonal.port.outbound.AuthorClientPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuthorClientConfigurationTest {

    @Nested
    @SpringBootTest
    @TestPropertySource(properties = {
        "com.tdrury.springlearning.hexagonal.adapters.authorclient.mock=true"
    })
    class MockAuthorClientConfiguration {
        @Autowired
        AuthorClientPort authorClientPort;

        @Test
        void configuration() {
            assertThat(authorClientPort, isA(MockAuthorClientAdapter.class));
        }
    }

    @Nested
    @SpringBootTest
    @TestPropertySource(properties = {
        "com.tdrury.springlearning.hexagonal.adapters.authorclient.mock=false"
    })
    class RealAuthorClientConfiguration {
        @Autowired
        AuthorClientPort authorClientPort;

        @Test
        void configuration() {
            assertThat(authorClientPort, isA(AuthorClientAdapter.class));
        }
    }
}
