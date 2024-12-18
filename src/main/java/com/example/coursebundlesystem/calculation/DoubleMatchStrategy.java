package com.example.coursebundlesystem.calculation;

import java.util.*;

public class DoubleMatchStrategy implements QuoteCalculationStrategy {

    @Override
    public double calculate(Map.Entry<String, String> provider, List<Map.Entry<String, Integer>> topTopics, Map<String, Integer> topicImportance) {
        Set<String> offeredTopics = new HashSet<>(Arrays.asList(provider.getValue().split("\\+")));
        int totalMatchContent = 0;
        int matchCount = 0;

        for (Map.Entry<String, Integer> topic : topTopics) {
            if (offeredTopics.contains(topic.getKey())) {
                totalMatchContent += topic.getValue();
                matchCount++;
            }
        }
        if (matchCount == 2) {
            return totalMatchContent * 0.1;
        }
        return 0.0;
    }
}
