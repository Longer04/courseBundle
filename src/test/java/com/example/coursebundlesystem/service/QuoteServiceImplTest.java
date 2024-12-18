package com.example.coursebundlesystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QuoteServiceImplTest {

    private QuoteServiceImpl quoteService;

    @BeforeEach
    void setUp() {
        Map<String, String> providerTopics = Map.of(
                "provider_a", "math+science",
                "provider_b", "reading+science",
                "provider_c", "history+math"
        );
        int topN = 3;
        int[] percentages = {20, 25, 30};
        quoteService = new QuoteServiceImpl(providerTopics, topN, percentages);
    }

    @Test
    void calculateQuotes() {
        Map<String, Map<String, Integer>> teacherRequest = Map.of(
                "topics", Map.of(
                        "reading", 20,
                        "math", 50,
                        "science", 30,
                        "history", 15,
                        "art", 10
                )
        );

        Map<String, Double> result = quoteService.calculateQuotes(teacherRequest);

        assertEquals(8.0, result.get("provider_a"));
        assertEquals(5.0, result.get("provider_b"));
        assertEquals(10, result.get("provider_c"));
    }
}