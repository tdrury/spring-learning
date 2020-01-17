package com.tdrury.springlearning.data.jpa.controller;

import com.spotify.hamcrest.pojo.IsPojo;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tdrury.springlearning.data.jpa.model.Matchers.authorPojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerRestTest {

    @Autowired private AuthorRepository authorRepository;
    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    private IsPojo<Author>[] authors; // test data POJO matchers

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
        assertThat(response.getBody(), is(authors[0]));
    }

    @Disabled
    @Test
    public void whenFindAll_thenReturnAll() {
        // given
        String url = getBaseUrl()+"/authors";
        log.debug("whenFindAll_thenReturnAll: call GET {}", url);

        // when
//        ResponseEntity<Author[]> response  = restTemplate.getForEntity(url, Author[].class);
        ResponseEntity<AuthorResponse> response  = restTemplate.getForEntity(url, AuthorResponse.class);
//        ResponseEntity<List<Author>> response  = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Author>>() { });
        log.debug("whenFindAll_thenReturnAll: response status={} body={}", response.getStatusCodeValue(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getAuthors(), containsInAnyOrder(authors));
    }

//    @Test
//    public void whenFindByLastName_thenReturnAuthor() {
//        // given
//
//        // when
//        String url = getBaseUrl()+"/search/findByLastName";
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//            .queryParam("lastName", "Weasley");
//        ResponseEntity<Author[]> response  = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Author[].class);
////        ResponseEntity<List<Author>> response  = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Author>>() { });
//
//        // then
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//        assertThat(response.getBody(), is(notNullValue()));
//        assertThat(response.getBody().length, is(2));
//        //assertThat(response.getBody().getFirstName(), is("Fred"));
//    }

    protected String getBaseUrl() {
        return "http://localhost:"+port+BASE_PATH;
    }

    @BeforeEach
    public void setupData() {
        authorRepository.deleteAll();
        authors = new IsPojo[3];
        authors[0] = authorPojo(authorRepository.saveAndFlush(new Author("Fred", "Weasley")));
        authors[1] = authorPojo(authorRepository.saveAndFlush(new Author("George", "Weasley")));
        authors[2] = authorPojo(authorRepository.saveAndFlush(new Author("George", "Burdell")));
    }

    @Data
    static class AuthorResponse {
        private List<Author> authors;
    }
}
