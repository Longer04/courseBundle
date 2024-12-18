package com.example.coursebundlesystem.calculation;

import java.util.List;
import java.util.Map;

public interface QuoteCalculationStrategy {
    double calculate(Map.Entry<String, String> provider, List<Map.Entry<String, Integer>> topTopics, Map<String, Integer> topicImportance);

}
