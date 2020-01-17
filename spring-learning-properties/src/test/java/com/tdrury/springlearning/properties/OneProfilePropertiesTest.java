package com.tdrury.springlearning.properties;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.env.Environment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@ActiveProfiles("p1")
@SpringBootTest
public class OneProfilePropertiesTest {

    @Autowired
    private Environment env;

    @Value("${tdrury.string1}")
    private String s1;

    @Value("${tdrury.p1.s}")
    private String p1s;

    @Test
    public void whenProfile1Active_thenOnlySeeValuesFromProfile1() {
        // expect
        assertThat(s1, is("p1 string"));
        assertThat(p1s, is("defined only in p1"));
    }

    @Test
    public void whenProfileNotActive_thenPropertiesAreUndefined() {
        // expect
        assertThat(env.getProperty("tdrury.p2.s"), is(nullValue()));
    }
}
