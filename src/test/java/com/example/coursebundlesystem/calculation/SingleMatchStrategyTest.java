package com.example.coursebundlesystem.calculation;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SingleMatchStrategyTest {

    @Test
    public void testSingleMatch() {
        SingleMatchStrategy strategy = new SingleMatchStrategy();

        Map.Entry<String, String> provider = Map.entry("provider_a", "reading+science");
        List<Map.Entry<String, Integer>> topTopics = List.of(
                Map.entry("reading", 20),
                Map.entry("math", 50),
                Map.entry("science", 30)
        );
        Map<String, Integer> topicImportance = Map.of(
                "reading", 30,
                "math", 25,
                "science", 20
        );

        double result = strategy.calculate(provider, topTopics, topicImportance);
        assertEquals(6.0, result);
    }
}