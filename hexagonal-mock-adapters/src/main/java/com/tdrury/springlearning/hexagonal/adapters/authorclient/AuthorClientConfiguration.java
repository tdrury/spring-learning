package com.tdrury.springlearning.hexagonal.adapters.authorclient;

import com.tdrury.springlearning.hexagonal.port.outbound.AuthorClientPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AuthorClientConfiguration {

    @Configuration
    public static class MockAuthorClientConfiguration {

        @Bean("authorClient")
        @ConditionalOnProperty(name="com.tdrury.springlearning.hexagonal.adapters.authorclient.mock", havingValue="true")
        AuthorClientPort authorClientPort() {
            log.info("AuthorClientConfiguration: using MOCK adapter");
            return new MockAuthorClientAdapter();
        }
    }

    @Configuration
    public static class RealAuthorClientConfiguration {

        @Bean("authorClient")
        @ConditionalOnProperty(name="com.tdrury.springlearning.hexagonal.adapters.authorclient.mock", havingValue="false")
        AuthorClientPort authorClientPort() {
            log.info("AuthorClientConfiguration: using REAL adapter");
            return new AuthorClientAdapter();
        }
    }

}
