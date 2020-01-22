package com.tdrury.springlearning.data.jpa.model;

import com.spotify.hamcrest.pojo.IsPojo;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.Matchers.is;

public class Matchers {

    public static IsPojo<Author> authorPojo(Author author) {
        return pojo(Author.class)
                .withProperty("firstName", is(author.getFirstName()))
                .withProperty("lastName", is(author.getLastName()));
    }

    public static IsPojo<Book> bookPojo(Book book) {
        return pojo(Book.class)
                .withProperty("isbn", is(book.getIsbn()));
    }
}
