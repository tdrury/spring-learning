package com.tdrury.springlearning.security.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
class UserInfoRepositoryTest {

    @Autowired
    UserInfoRepository userInfoRepository;


    @Test
    void findByEmail_givenUserNotExist_thenReturnNothing() {
        // given
        UserInfo user1 = new UserInfo("foo@foo.com");
        UserInfo user2 = new UserInfo("bar@bar.com");
        userInfoRepository.saveAll(Arrays.asList(user1, user2));

        // when
        UserInfo found = userInfoRepository.findByEmail("notfound@foo.com");

        // then
        assertThat(found, is(nullValue()));
    }

    @Test
    void findByEmail_givenUsersExists_thenReturnUser() {
        // given
        UserInfo user1 = new UserInfo("foo@foo.com");
        UserInfo user2 = new UserInfo("bar@bar.com");
        userInfoRepository.saveAll(Arrays.asList(user1, user2));

        // when
        UserInfo found = userInfoRepository.findByEmail("bar@bar.com");

        // then
        assertThat(found, is(notNullValue()));
        assertThat(found.getEmail(), is("bar@bar.com"));
    }

}
