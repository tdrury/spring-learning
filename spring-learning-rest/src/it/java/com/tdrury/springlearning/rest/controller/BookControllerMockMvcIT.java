package com.tdrury.springlearning.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest
class BookControllerMockMvcIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void post_whenNonJsonInput_thenException() throws Exception {
        // given
        byte[] someBinary = new byte[]{1, 2, 3};
        mockMvc.perform(post("/api/books")
                        .content(someBinary)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isBadRequest())
;
    }

}
