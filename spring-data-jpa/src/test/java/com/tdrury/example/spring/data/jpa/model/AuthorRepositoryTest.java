package com.tdrury.example.spring.data.jpa.model;

import com.spotify.hamcrest.pojo.IsPojo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
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
        Optional<Author> opt = authorRepository.findById(1L);
        Author a = opt.get();

        // then
        assertThat(opt.isPresent(), is(true));
        assertThat(a.getFirstName(), is("Fred"));
        assertThat(a.getLastName(), is("Weasley"));
    }

    @Test
    public void findByLastName() {
        // when
        List<Author> authors = authorRepository.findByLastName("Weasley");

        // then
        assertThat(authors.size(), is(2));
        assertThat(authors, containsInAnyOrder(authorPojo("Fred", "Weasley"), authorPojo("George", "Weasley")));
    }

    @Test
    public void findByLastName_notFound() {
        // when
        List<Author> authors = authorRepository.findByLastName("DoesNotExist");

        // then
        assertThat(authors.size(), is(0));
    }

    private IsPojo<Author> authorPojo(String firstName, String lastName) {
         return pojo(Author.class)
                 .withProperty("firstName", is(firstName))
                 .withProperty("lastName", is(lastName));
    }

    @BeforeEach
    public void init() {
        authorRepository.deleteAll();
        authorRepository.save(new Author("Fred", "Weasley"));
        authorRepository.save(new Author("George", "Weasley"));
        authorRepository.save(new Author("George", "Burdell"));
    }
}
