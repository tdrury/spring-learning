package com.tdrury.example.spring.data.jpa.model;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Matchers {

    public static Matcher<Author> hasSameName(Author expected) {
        return new TypeSafeMatcher<Author>() {
            @Override
            protected boolean matchesSafely(Author actual) {
                return (actual.getFirstName().equals(expected.getFirstName())) && (actual.getLastName().equals(expected.getFirstName()));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("should match ").appendValue(expected);
            }
        };
    }
}
