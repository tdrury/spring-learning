package com.tdrury.springlearning.data.jpa.controller;

import com.spotify.hamcrest.pojo.IsPojo;
import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.tdrury.springlearning.data.jpa.model.Matchers.authorPojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerHateoasRestTest {

    @Autowired private AuthorRepository authorRepository;

    @LocalServerPort private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    private IsPojo<Author>[] authors; // test data POJO matchers

    @Test
    public void whenFindById_thenReturnAuthor() {
        // given
        String url = getBaseUrl();
        log.debug("whenGetAllAuthors_thenReturnAllAuthors: calling GET {}", url);
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<EntityModel<Author>> authorType = new ParameterizedTypeReference<EntityModel<Author>>() {};

        // when
        EntityModel<Author> author = traverson.follow("authors/1").toObject(authorType);
        log.debug("whenFindById_thenReturnAuthor: author={}", author);

        // then
        assertThat(author.getContent(), is(authors[0]));
    }

    @Test
    public void whenGetAuthorsByLastName_thenReturnAllAuthorsWithSameLastName() {
        // given
        String url = getBaseUrl();
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<CollectionModel<Author>>() {};
        Map<String,Object> params = new HashMap<>();
        params.put("lastName", "Weasley");

        // when
        CollectionModel<Author> response = traverson
                .follow("authors", "search", "findByLastName")
                .withTemplateParameters(params)
                .toObject(authorsType);
        log.debug("whenGetAuthorsByLastName_thenReturnAllAuthorsWithSameLastName: got {}", response);

        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getContent().size(), is(2));
        assertThat(response.getContent(), containsInAnyOrder(authors[0], authors[1]));
    }

    @Test
    public void whenGetAllAuthors_thenReturnAllAuthors() {
        // given
        String url = getBaseUrl();
        log.debug("whenGetAllAuthors_thenReturnAllAuthors: calling GET {}", url);
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<CollectionModel<Author>>() {};

        // when
        CollectionModel<Author> response = traverson.follow("authors").toObject(authorsType);
        log.debug("whenGetAllAuthors_thenReturnAllAuthors: got {}", response);

        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getContent().size(), is(3));
        assertThat(response.getContent(), containsInAnyOrder(authors));
    }

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
}
