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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("dev")
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void findById() {
        // when
        Optional<Author> response = authorRepository.findById(1L);
        Author a = response.orElse(null);

        // then
        assertThat(a, is(authorMatcher(authors[0])));
    }

    @Test
    public void findByLastName() {
        // when
        List<Author> response = authorRepository.findByLastName("Weasley");

        // then
        assertThat(response.size(), is(2));
        assertThat(response, containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
    }

    @Test
    public void findByLastName_notFound() {
        // when
        List<Author> response = authorRepository.findByLastName("DoesNotExist");

        // then
        assertThat(response.size(), is(0));
    }

    private Author[] authors;

    @BeforeEach
    public void init() {
        authorRepository.deleteAll();
        authors = new Author[3];
        authors[0] = new Author("Fred", "Weasley");
        authors[1] = new Author("George", "Weasley");
        authors[2] = new Author("George", "Burdell");
        authorRepository.saveAll(Arrays.asList(authors));
    }
}
