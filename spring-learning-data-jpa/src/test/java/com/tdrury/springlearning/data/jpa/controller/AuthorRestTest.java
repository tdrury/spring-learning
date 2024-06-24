package com.tdrury.springlearning.data.jpa.controller;

import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.AuthorRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorRestTest {

    @Autowired private AuthorRepository authorRepository;
    private RestClient restClient;

    @LocalServerPort
    private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    @Test
    void findById_whenFirstId_thenReturnFirstAuthor() {
        // given
        String url = getBaseUrl()+"/authors/1";
        log.debug("findById_whenFirstId_thenReturnFirstAuthor: call GET {}", url);

        // when
        ResponseEntity<Author> response = restClient.get().uri(url).retrieve().toEntity(Author.class);
        log.debug("findById_whenFirstId_thenReturnFirstAuthor: response status={} body={}", response.getStatusCode(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(authorMatcher(testAuthors[0])));
    }

    @Test
    @Disabled
    // this does not work as hateoas responses are intended to be navigated.
    // see authors.http "GET authors" response
    public void findAll_whenNoFilter_thenReturnAll() {
        // given
        String url = getBaseUrl()+"/authors";
        log.debug("findAll_whenNoFilter_thenReturnAll: call GET {}", url);

        // when
        ResponseEntity<Author[]> response  = restClient.get().uri(url).retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});
        log.debug("findAll_whenNoFilter_thenReturnAll: response status={} body={}", response.getStatusCode(), response.getBody());
        Author[] authorResponse = response.getBody();
        assertThat(authorResponse, is(notNullValue()));

        List<Author> authorList = Arrays.asList(authorResponse);
        assertThat(authorList, is(notNullValue()));
        for (Author a : authorList) {
            log.debug("findAll_whenNoFilter_thenReturnAll: got author: {}", a);
        }

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(authorList, is(notNullValue()));
        assertThat(authorList, containsInAnyOrder(testAuthors));
    }

    @Test
    @DirtiesContext
    void createAuthor_whenPOSTAuthor_thenAuthorCreated() {
        // given
        Author shakespeare = new Author("William", "Shakespeare");
        String url = getBaseUrl()+"/authors";
        log.debug("createAuthor_whenPOSTAuthor_thenAuthorCreated: call POST {}", url);

        // when
        ResponseEntity<Void> response = restClient.post().uri(url).contentType(MediaType.APPLICATION_JSON)
                .body(shakespeare).retrieve().toBodilessEntity();

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    protected String getBaseUrl() {
        return "http://localhost:"+port+BASE_PATH;
    }

    @BeforeEach
    public void setupRestClient() {
        restClient = RestClient.builder()
                        .requestFactory(new HttpComponentsClientHttpRequestFactory())
                        .build();
    }

    Author[] testAuthors;

    @BeforeEach
    public void setupData() {
        authorRepository.deleteAll();
        testAuthors = new Author[] {
            new Author("Fred", "Weasley"),
            new Author("George", "Weasley"),
            new Author("George", "Burdell")
        };
        log.info("creating setup data:\n\t{}", Arrays.asList(testAuthors));
        authorRepository.saveAll(Arrays.asList(testAuthors));
    }

    @Data
    static class AuthorResponse {
        private List<Author> authors;
    }
}
