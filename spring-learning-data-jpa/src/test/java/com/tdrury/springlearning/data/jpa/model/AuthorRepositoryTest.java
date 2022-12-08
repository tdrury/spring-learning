package com.tdrury.springlearning.data.jpa.model;

import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("dev")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void whenFindById_thenReturnSingleAuthor() {
        // when
        Optional<Author> response = authorRepository.findById(authors[0].getId());
        Author a = response.orElse(null);

        // then
        assertThat(a, is(authorMatcher(authors[0])));
    }

    @Test
    void whenFindById_givenIdDoesNotExist_thenReturnNothing() {
        // when
        Optional<Author> response = authorRepository.findById(999L);
        Author a = response.orElse(null);

        // then
        assertThat(a, is(nullValue()));
    }

    @Test
    void whenGetOne_givenIdDoesNotExist_thenThrowException() {
        // when
        Author author = authorRepository.getOne(999L);
        // entity is lazily fetched so you don't get an exception calling getOne() but you get it
        // when trying to read the entity.

        // then
        assertThrows(EntityNotFoundException.class,
                () -> assertThat(author, is(nullValue()))
        );
    }

    @Test
    void whenFindByLastName_givenMultipleMatches_thenReturnAllMatches() {
        // when
        List<Author> response = authorRepository.findByLastName("Weasley");

        // then
        assertThat(response.size(), is(2));
        assertThat(response, containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
    }

    @Test
    void whenFindByLastName_givenDoesNotExist_thenReturnNothing() {
        // when
        List<Author> response = authorRepository.findByLastName("DoesNotExist");

        // then
        assertThat(response.size(), is(0));
    }

    @Test
    void whenSaveAuthor_thenAuthorIsInDatabase() {
        // given
        Author a = new Author("Brandon", "Sanderson");

        // when
        Author saved = authorRepository.save(a);

        // then
        Author retrieved = authorRepository.getOne(saved.getId());
        assertThat(retrieved, authorMatcher(a));
    }

    @Test
    void whenDeleteAuthor_thenAuthorIsRemovedFromDatabase_usingGetOne() {
        // given
        Long id = authors[0].getId();

        // when
        authorRepository.delete(authors[0]);

        // then
        assertThrows(org.springframework.orm.jpa.JpaObjectRetrievalFailureException.class,
                () -> authorRepository.getOne(id)
        );
    }

    @Test
    void whenDeleteAuthor_thenAuthorIsRemovedFromDatabase_usingFindById() {
        // given
        Long id = authors[0].getId();

        // when
        authorRepository.delete(authors[0]);

        // then
        Optional<Author> response = authorRepository.findById(id);
        assertThat(response.isPresent(), is(false));
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
