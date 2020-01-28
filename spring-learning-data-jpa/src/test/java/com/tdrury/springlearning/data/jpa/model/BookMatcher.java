package com.tdrury.springlearning.data.jpa.model;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

public class BookMatcher extends TypeSafeMatcher<Book> {

    private final Book expected;

    public BookMatcher(Book expected) {
        this.expected = expected;
    }
    
    @Override
    protected boolean matchesSafely(Book actual) {
        if (!actual.getTitle().equals(expected.getTitle())) return false;
        if (!actual.getIsbn().equals(expected.getIsbn())) return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    public static Matcher<Book> bookMatcher(Book book) {
        return new BookMatcher(book);
    }

    public static List<Matcher<Book>> bookMatchers(Book [] books) {
        List<Matcher<Book>> matchers = new ArrayList<>();
        for (Book b : books) {
            matchers.add(new BookMatcher(b));
        }
        return matchers;
    }
}
