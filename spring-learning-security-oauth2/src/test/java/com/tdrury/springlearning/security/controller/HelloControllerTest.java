package com.tdrury.springlearning.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class HelloControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/public"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("public page"));
    }

    @Test
    @WithMockUser(username="user", password="password")
    void privatePage_givenValidUser_thenGetContent() throws Exception {
        mockMvc.perform(get("/private"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("private page"));
    }


    @Test
    @WithAnonymousUser
    void privatePage_givenAnonymousUser_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/private"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void privatePage_givenNoUser_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/private"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void noPage_thenForbidden() throws Exception {
        mockMvc.perform(get("/doesnotexist"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
