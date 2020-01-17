package com.tdrury.springlearning.properties;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
@ActiveProfiles("p1, p2")
@SpringBootTest
public class TwoProfilesPropertiesTest {

    @Value("${tdrury.string1}")
    private String s1;

    @Value("${tdrury.p1.s}")
    private String p1s;

    @Value("${tdrury.p2.s}")
    private String p2s;

    @Test
    public void whenBothProfilesActive_thenLaterProfilesOverwriteEarlierProfiles() {
        // expect
        assertThat(s1, is("p2 string"));
        assertThat(p1s, is("defined only in p1"));
        assertThat(p2s, is("defined only in p2"));
    }
}
