package com.tdrury.example.spring.data.jpa.controller;

import com.tdrury.example.spring.data.jpa.model.Author;
import com.tdrury.example.spring.data.jpa.model.AuthorRepository;
import com.tdrury.example.spring.data.jpa.model.Matchers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static com.tdrury.example.spring.data.jpa.model.Matchers.*;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerRestTest {

    @Autowired private AuthorRepository authorRepository;
    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    private Author[] authors; // test data

    @Test
    public void whenFindByIdStandard_thenReturnAuthor() {
        // given
        String url = getBaseUrl()+"/authors/1";
        log.debug("whenFindById_thenReturnAuthor: call GET {}", url);

        // when
        ResponseEntity<Author> response  = restTemplate.getForEntity(url, Author.class);
        log.debug("whenFindById_thenReturnAuthor: response status={} body={}", response.getStatusCodeValue(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSameName(authors[0]));
    }

    @Test
    public void whenFindById_thenReturnAuthor() {
        // given
        String url = getBaseUrl()+"/authors/1";
        log.debug("whenFindById_thenReturnAuthor: call GET {}", url);

        // when
        ResponseEntity<Author> response  = restTemplate.getForEntity(url, Author.class);
//        Traverson traverson = new Traverson(URI.create(BASE_PATH), MediaTypes.HAL_JSON);
//        ParameterizedTypeReference<EntityModel<Author>> authorType = new ParameterizedTypeReference<EntityModel<Author>>() {};
//        EntityModel<Author> author = traverson.follow("/authors/1").toObject(authorType);
//        log.debug("whenFindById_thenReturnAuthor: author={}", author);
        log.debug("whenFindById_thenReturnAuthor: response status={} body={}", response.getStatusCodeValue(), response.getBody());

        // then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSameName(authors[0]));
    }

//    @Test
//    public void whenFindAuthorById_thenReturnAuthor() {
//        // given
//
//        // when
//        String url = getBaseUrl();
//        log.debug("whenGetAuthorById_thenReturnAuthor: calling GET {}", url);
//        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
//        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<CollectionModel<Author>>() {};
//        CollectionModel<Author> response = traverson.follow("authors", "search", "by-id").toObject(authorsType);
//        log.debug("whenGetAuthorById_thenReturnAuthor: got {}", response);
//        Author[] authors = response.getContent().toArray(new Author[1]);
//
//        // then
//        assertThat(response.getContent().size(), is(1));
//        assertThat(authors[0].getFirstName(), is("Fred"));
//        assertThat(authors[0].getLastName(), is("Weasley"));
//    }

    @Test
    public void whenGetAllAuthors_thenReturnAllAuthors() {
        // given
        String url = getBaseUrl();
        log.debug("whenGetAllAuthors_thenReturnAllAuthors: calling GET {}", url);

        // when
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<CollectionModel<Author>>() {};
        CollectionModel<Author> response = traverson.follow("authors").toObject(authorsType);
        log.debug("whenGetAllAuthors_thenReturnAllAuthors: got {}", response);

        // then
        assertThat(response.getContent().size(), is(3));
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

    @BeforeAll
    public void setupData() {
        authorRepository.deleteAll();
        authors = new Author[3];
        authors[0] = authorRepository.saveAndFlush(new Author("Fred", "Weasley"));
        authors[1] = authorRepository.saveAndFlush(new Author("George", "Weasley"));
        authors[2] = authorRepository.saveAndFlush(new Author("George", "Burdell"));
    }
}
