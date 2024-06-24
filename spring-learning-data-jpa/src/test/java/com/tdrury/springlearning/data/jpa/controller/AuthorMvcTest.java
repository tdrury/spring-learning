package com.tdrury.springlearning.data.jpa.controller;

import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthorMvcTest {

    @Autowired MockMvc mockMvc;
    @Autowired private AuthorRepository authorRepository;

    @Test
    void findAll_when3AuthorsInRepo_thenReturnAllAuthors() throws Exception {
        // given
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*.authors.*", hasSize(3)))
                .andExpect(jsonPath("*.authors[*].firstName").value(containsInAnyOrder("Fred", "George", "George")))
                .andExpect(jsonPath("*.authors[*].lastName").value(containsInAnyOrder("Weasley", "Weasley", "Burdell")))
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.totalElements").value(3))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(1))
        ;
    }

    @Test
    void findById_whenIdGiven_thenReturnAuthor() throws Exception {
        // given
        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("Fred"))
                .andExpect(jsonPath("$.lastName").value("Weasley"))
                ;
    }

    @BeforeEach
    public void setupData() {
        authorRepository.deleteAll();
        Author[] authors = new Author[] {
                new Author("Fred", "Weasley"),
                new Author("George", "Weasley"),
                new Author("George", "Burdell")
        };
        authorRepository.saveAll(Arrays.asList(authors));
        List<Author> all = authorRepository.findAll();
        log.info("setupData: all={}", all);
    }
}
