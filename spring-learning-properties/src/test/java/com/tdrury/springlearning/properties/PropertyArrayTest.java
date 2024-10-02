package com.tdrury.springlearning.properties;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@SpringBootTest
class PropertyArrayTest {

    @Autowired
    ThingConfig things;

    @Test
    void whenArrayOfProperties_thenCanLoadThemAll() {
        assertThat(things, is(notNullValue()));
        log.info("things={}", things);
        assertThat(things.getThings().size(), is(2));
        assertThat(things.getThings().get(0).getName(), is("thing1"));
        assertThat(things.getThings().get(0).getValue(), is("10"));
        assertThat(things.getThings().get(1).getName(), is("thing2"));
        assertThat(things.getThings().get(1).getValue(), is("20"));
    }

}


