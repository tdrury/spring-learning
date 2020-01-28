package com.tdrury.springlearning.data.jpa.model;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

public class AuthorMatcher extends TypeSafeMatcher<Author> {

    private final Author expected;

    public AuthorMatcher(Author expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(Author actual) {
        if (!actual.getFirstName().equals(expected.getFirstName())) return false;
        if (!actual.getLastName().equals(expected.getLastName())) return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    public static Matcher<Author> authorMatcher(Author author) {
        return new AuthorMatcher(author);
    }

    public static List<Matcher<Author>> authorMatchers(Author[] authors) {
        List<Matcher<Author>> matchers = new ArrayList<>();
        for (Author a : authors) {
            matchers.add(new AuthorMatcher(a));
        }
        return matchers;
    }
}
