package com.tdrury.springlearning.data.jpa.controller;

import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.AuthorRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerRestTest {

    @Autowired private AuthorRepository authorRepository;
    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    @Test
    public void whenFindById_thenReturnAuthor() {
        // given
        String url = getBaseUrl()+"/authors/1";
        log.debug("whenFindById_thenReturnAuthor: call GET {}", url);

        // when
        ResponseEntity<Author> response  = restTemplate.getForEntity(url, Author.class);
        log.debug("whenFindById_thenReturnAuthor: response status={} body={}", response.getStatusCodeValue(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(authorMatcher(authors[0])));
    }

    // not sure we can use RestTemplate to read collection of HATEOAS-formatted entities.
    // See AuthorControllerHateoasRestTest for reading Collections of entities.

//    @Test
//    public void whenFindAll_thenReturnAll() {
//        // given
//        String url = getBaseUrl()+"/authors";
//        log.debug("whenFindAll_thenReturnAll: call GET {}", url);
//
//        // when
//        ResponseEntity<AuthorResponse> response  = restTemplate.getForEntity(url, AuthorResponse.class);
//        log.debug("whenFindAll_thenReturnAll: response status={} body={}", response.getStatusCodeValue(), response.getBody());
//        AuthorResponse content = response.getBody();
//        for (Author a : content.getAuthors()) {
//            log.debug("whenFindAll_thenReturnAll: got author: {}", a);
//        }
//
//        // then
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(content, is(notNullValue()));
//        assertThat(content, containsInAnyOrder((Collection)authorMatchers(authors)));
//    }

    @Test
    public void testWhenPOSTAuthor_thenAuthorCreated() {
        // given
        Author a = new Author("William", "Shakespeare");
        String url = getBaseUrl()+"/authors";
        log.debug("whenFindAll_thenReturnAll: call GET {}", url);

        // when
        HttpEntity<Author> request = new HttpEntity<>(a);
        ResponseEntity<Author> response = restTemplate.exchange(url, HttpMethod.POST, request, Author.class);

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(authorMatcher(a)));
    }


    protected String getBaseUrl() {
        return "http://localhost:"+port+BASE_PATH;
    }

    Author[] authors;

    @BeforeEach
    public void setupData() {
        authorRepository.deleteAll();
        authors = new Author[] {
            new Author("Fred", "Weasley"),
            new Author("George", "Weasley"),
            new Author("George", "Burdell")
        };
        authorRepository.saveAll(Arrays.asList(authors));
    }

    @Data
    static class AuthorResponse {
        private List<Author> authors;
    }
}
