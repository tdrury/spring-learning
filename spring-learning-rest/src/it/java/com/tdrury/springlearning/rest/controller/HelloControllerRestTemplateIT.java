package com.tdrury.springlearning.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerRestTemplateIT {

    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    @Test
    public void whenHelloWithNoName_thenReturnHelloOnly() {
        // given
        String url = getBaseUrl()+"/hello";
        log.debug("whenHelloWithNoName_thenReturnHelloOnly: call GET {}", url);

        // when
        ResponseEntity<String> response  = restTemplate.getForEntity(url, String.class);
        log.debug("whenHelloWithNoName_thenReturnHelloOnly: response status={} body={}", response.getStatusCodeValue(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is("Hello"));

    }

    protected String getBaseUrl() {
        return "http://localhost:"+port+BASE_PATH;
    }

}
