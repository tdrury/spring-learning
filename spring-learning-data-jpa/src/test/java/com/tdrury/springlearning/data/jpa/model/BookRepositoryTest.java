package com.tdrury.springlearning.data.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tdrury.springlearning.data.jpa.model.AuthorMatcher.authorMatcher;
import static com.tdrury.springlearning.data.jpa.model.BookMatcher.bookMatcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("dev")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    @Test
    void whenGetBookWithSingleAuthor_thenReturnCorrectBook() {
        // when
        List<Book> response = bookRepository.findByTitle("GT a History");

        // then
        assertThat(response.size(), is(1));
        assertThat(response.get(0).getIsbn(), is("isbn3"));
        assertThat(response.get(0).getAuthors().size(), is(1));
        assertThat(response.get(0).getAuthors().get(0), is(authorMatcher(authors[2])));
    }

    @Test
    void whenGetBookWithMultipleAuthors_thenReturnCorrectBook() {
        // when
        List<Book> response = bookRepository.findByTitle("Jokes");

        // then
        assertThat(response.size(), is(1));
        assertThat(response.get(0).getIsbn(), is("isbn1"));
        assertThat(response.get(0).getAuthors().size(), is(2));
        assertThat(response.get(0).getAuthors(), containsInAnyOrder(authorMatcher(authors[0]), authorMatcher(authors[1])));
    }

    @Test
    void whenGetBookByAuthor_thenReturnCorrectBooks() {
        // when
        List<Book> response = bookRepository.findByAuthorsContaining(authors[0]);

        // then
        assertThat(response.size(), is(2));
        assertThat(response, containsInAnyOrder(bookMatcher(books[0]), bookMatcher(books[1])));
    }

    @Test
    void whenGetAuthor_thenReturnedAuthorHasBooks() {
        // when
        Author author = authorRepository.getOne(authors[0].getId());
        System.out.println("author="+author);
        List<Book> authorBooks = author.getBooks();

        // then
        assertThat(authorBooks.size(), is(2));
        assertThat(authorBooks, containsInAnyOrder(bookMatcher(books[0]), bookMatcher(books[1])));
    }

    Author[] authors;
    Book[] books;

    @BeforeEach
    public void init() {

        authors = new Author[] {
            new Author("Fred", "Weasley"),
            new Author("George", "Weasley"),
            new Author("George", "Burdell")
        };
        authorRepository.saveAll(Arrays.asList(authors));

        books = new Book[3];
        books[0] = new Book("isbn1", "Jokes");
        books[0].setAuthors(Arrays.asList(authors[0], authors[1]));

        books[1] = new Book("isbn2", "More Jokes");
        books[1].setAuthors(Arrays.asList(authors[0], authors[1]));

        books[2] = new Book("isbn3", "GT a History");
        books[2].setAuthors(Collections.singletonList(authors[2]));

        bookRepository.saveAll(Arrays.asList(books));

        authorRepository.getOne(authors[0].getId()).setBooks(Arrays.asList(books[0], books[1]));
        authorRepository.getOne(authors[1].getId()).setBooks(Arrays.asList(books[0], books[1]));
        authorRepository.getOne(authors[2].getId()).setBooks(Collections.singletonList(books[2]));
    }
}
