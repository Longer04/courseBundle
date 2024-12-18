package com.example.coursebundlesystem.calculation;

import java.util.*;

public class SingleMatchStrategy implements QuoteCalculationStrategy {

    @Override
    public double calculate(Map.Entry<String, String> provider, List<Map.Entry<String, Integer>> topTopics, Map<String, Integer> topicImportance) {
        Set<String> offeredTopics = new HashSet<>(Arrays.asList(provider.getValue().split("\\+")));
        double quote = 0.0;

        for (Map.Entry<String, Integer> topic : topTopics) {
            Integer importance = topicImportance.get(topic.getKey());
            if (offeredTopics.contains(topic.getKey()) && importance != null) {
                quote = (importance / 100.0) * topic.getValue();
                break;
            }
        }
        return quote;
    }
}
