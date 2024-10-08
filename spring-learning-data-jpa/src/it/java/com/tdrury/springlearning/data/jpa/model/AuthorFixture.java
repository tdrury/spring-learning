package com.tdrury.springlearning.data.jpa.model;

import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class AuthorFixture {

    @When("^Author with name (.+) (.+) is created and saved$")
    public void createAuthorAndSave(String firstName, String lastName) {
        Author a = new Author(firstName, lastName);
        HttpEntity<Author> entity = new HttpEntity<Author>(a, null);
        String url = "http://localhost:8080/authors";
        buildRestTemplate().postForEntity(url, entity, Author.class);
    }

    @When("^Searching for Authors with last name (.+) returns (\\d+) result$")
    public void findAuthorsByLastName(String lastName, int expectedCount) {
        String requestUri = "http://localhost:8080/authors";
        Map<String,String> urlParameters = new HashMap<String,String>();
        //todo add parameter for lastName
        ResponseEntity<Author[]> response = buildRestTemplate().getForEntity(requestUri, Author[].class, urlParameters);
        Author[] authors = response.getBody();
        assertThat(authors.length, is(expectedCount));
    }

    protected RestTemplate buildRestTemplate() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(30);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        //todo set headers
        return restTemplate;
    }
}
