package com.tdrury.springlearning.data.jpa.controller;

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
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatchers;
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

//    @Test
//    public void whenFindById_thenReturnAuthor() {
//        // given
//        String url = getBaseUrl();
//        log.debug("whenGetAllAuthors_thenReturnAllAuthors: calling GET {}", url);
//        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
//        ParameterizedTypeReference<EntityModel<Author>> authorType = new ParameterizedTypeReference<EntityModel<Author>>() {};
//
//        // when
//        EntityModel<Author> author = traverson.follow("authors/1").toObject(authorType);
//        log.debug("whenFindById_thenReturnAuthor: author={}", author);
//
//        // then
//        assertThat(author, is(notNullValue()));
//        assertThat(author.getContent(), is(authorPojo(authors[0])));
//    }

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
        assertThat(response.getContent(), containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
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
        Collection<Author> content = response.getContent();
        assertThat(content.size(), is(3));

        // two ways to verify content:
        assertThat(content, containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1]), authorMatcher(authors[2])));
        // must cast to Collection due to https://github.com/ht2/hamcrest/issues/188
        assertThat(content, containsInAnyOrder((Collection)authorMatchers(authors)));
    }

    protected String getBaseUrl() {
        return "http://localhost:"+port+BASE_PATH;
    }

    private Author[] authors; // test data

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
}
