package com.tdrury.springlearning.data.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("dev")
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void whenFindById_thenReturnSingleAuthor() {
        // when
        Optional<Author> response = authorRepository.findById(authors[0].getId());
        Author a = response.orElse(null);

        // then
        assertThat(a, is(authorMatcher(authors[0])));
    }

    @Test
    public void whenFindById_givenIdDoesNotExist_thenReturnNothing() {
        // when
        Optional<Author> response = authorRepository.findById(999L);
        Author a = response.orElse(null);

        // then
        assertThat(a, is(nullValue()));
    }

    @Test
    public void whenFindByLastName_givenMultipleMatches_thenReturnAllMatches() {
        // when
        List<Author> response = authorRepository.findByLastName("Weasley");

        // then
        assertThat(response.size(), is(2));
        assertThat(response, containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
    }

    @Test
    public void whenFindByLastName_givenDoesNotExist_thenReturnNothing() {
        // when
        List<Author> response = authorRepository.findByLastName("DoesNotExist");

        // then
        assertThat(response.size(), is(0));
    }

    @Test
    public void whenSaveAuthor_thenAuthorIsInDatabase() {
        // given
        Author a = new Author("Brandon", "Sanderson");

        // when
        Author saved = authorRepository.save(a);

        // then
        Author retrieved = authorRepository.getOne(saved.getId());
        assertThat(retrieved, authorMatcher(a));
    }

    private Author[] authors;

    @BeforeEach
    public void init() {
        authorRepository.deleteAll();
        authors = new Author[] {
            new Author("Fred", "Weasley"),
            new Author("George", "Weasley"),
            new Author("George", "Burdell")
        };
        authorRepository.saveAll(Arrays.asList(authors));
    }
}
