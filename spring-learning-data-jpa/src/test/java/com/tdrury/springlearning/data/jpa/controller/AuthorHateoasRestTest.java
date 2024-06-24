package com.tdrury.springlearning.data.jpa.controller;

import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.*;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorHateoasRestTest {

    @Autowired private AuthorRepository authorRepository;

    @LocalServerPort
    private int port;

    @Value("${spring.data.rest.base-path}")
    private String BASE_PATH;

    @Test
    // This does not work since the Author entity does not contain the additional hateoas links
    public void findById_whenAuthorExists_thenReturnAuthorWithNullParameters() {
        // given
        String url = getBaseUrl();
        log.debug("findById_whenAuthorExists_thenReturnAuthor: calling GET {}", url);
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);

        // when
        Author author = traverson.follow("authors")
                .withTemplateParameters(Map.of("id", "1")).toObject(Author.class);
        log.debug("findById_whenAuthorExists_thenReturnAuthor: author={}", author);
        // Author entity will be created, but all fields will be null

        // then
        assertThat(author, is(notNullValue()));
        assertThat(author.getId(), is(nullValue()));
    }

    @Test
    void getAuthorsByLastName_whenMultipleAuthorsWithSameLastName_thenReturnAllAuthorsWithSameLastName() {
        // given
        String url = getBaseUrl();
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<>() {};
        Map<String,Object> params = new HashMap<>();
        params.put("lastName", "Weasley");

        // when
        CollectionModel<Author> response = traverson
                .follow("authors", "search", "findByLastName")
                .withTemplateParameters(params)
                .toObject(authorsType);
        log.debug("getAuthorsByLastName_whenMultipleAuthorsWithSameLastName_thenReturnAllAuthorsWithSameLastName:\n\tgot {}", response);

        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getContent().size(), is(2));
        assertThat(response.getContent(), containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
    }

    @Test
    void getAllAuthors_whenMultipleAuthors_thenReturnAllAuthors() {
        // given
        String url = getBaseUrl();
        log.debug("getAllAuthors_whenMultipleAuthors_thenReturnAllAuthors: calling GET {}", url);
        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);
        ParameterizedTypeReference<CollectionModel<Author>> authorsType = new ParameterizedTypeReference<>() {};

        // when
        CollectionModel<Author> response = traverson.follow("authors").toObject(authorsType);
        log.debug("getAllAuthors_whenMultipleAuthors_thenReturnAllAuthors:\n\tgot {}", response);

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
        List<Author> all = authorRepository.findAll();
        log.info("setupData: all={}", all);
    }
}
