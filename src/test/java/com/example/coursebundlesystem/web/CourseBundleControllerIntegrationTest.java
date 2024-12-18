package com.example.coursebundlesystem.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CourseBundleControllerIntegrationTest {

    @Autowired
    private CourseBundleController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGenerateBundle() throws Exception {
        Map<String, Map<String, Integer>> request = Map.of(
                "topics", Map.of(
                        "reading", 20,
                        "math", 50,
                        "science", 30,
                        "history", 15,
                        "art", 10
                )
        );

        mockMvc.perform(post("/api/v1/bundles/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider_a").value(8.0))
                .andExpect(jsonPath("$.provider_b").value(5.0))
                .andExpect(jsonPath("$.provider_c").value(10));
    }
}