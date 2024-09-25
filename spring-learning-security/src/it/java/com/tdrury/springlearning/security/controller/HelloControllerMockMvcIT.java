package com.tdrury.springlearning.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class HelloControllerMockMvcIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username="user")
    void hello_givenUserNotAdmin_thenReturnHelloUsername() throws Exception {
        // given
        mockMvc.perform(get("/api/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, user"));
    }

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void hello_givenAdmin_thenReturnHelloUsername() throws Exception {
        // given
        mockMvc.perform(get("/api/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Admin, admin"));
    }

    @Test
    @WithAnonymousUser
    void hello_givenAnonymous_thenUnauthorized() throws Exception {
        // given
        mockMvc.perform(get("/api/hello"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }


}
