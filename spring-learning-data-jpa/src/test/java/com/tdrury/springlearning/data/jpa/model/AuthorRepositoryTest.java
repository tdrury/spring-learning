package com.tdrury.springlearning.data.jpa.model;

import com.spotify.hamcrest.pojo.IsPojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.tdrury.springlearning.data.jpa.model.Matchers.authorPojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("dev")
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private IsPojo<Author>[] authors; // test data POJO matchers

    @Test
    public void findById() {
        // when
        Optional<Author> response = authorRepository.findById(1L);
        Author a = response.get();

        // then
        assertThat(a, is(authors[0]));
    }

    @Test
    public void findByLastName() {
        // when
        List<Author> response = authorRepository.findByLastName("Weasley");

        // then
        assertThat(response.size(), is(2));
        assertThat(response, containsInAnyOrder(authors[0], authors[1]));
    }

    @Test
    public void findByLastName_notFound() {
        // when
        List<Author> response = authorRepository.findByLastName("DoesNotExist");

        // then
        assertThat(response.size(), is(0));
    }

    @BeforeEach
    public void init() {
        authorRepository.deleteAll();
        authors = new IsPojo[3];
        authors[0] = authorPojo(authorRepository.saveAndFlush(new Author("Fred", "Weasley")));
        authors[1] = authorPojo(authorRepository.saveAndFlush(new Author("George", "Weasley")));
        authors[2] = authorPojo(authorRepository.saveAndFlush(new Author("George", "Burdell")));
    }
}
