package com.example.coursebundlesystem.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class ProviderConfiguration {

    @Value("classpath:provider-topics.json")
    private InputStream providerConfigJson;

    @Value("${quote.topicsCount:3}")
    private int topicsCount;

    @Value("${quote.percentages:20,25,30}")
    private String percentages;

    @Bean
    public Map<String, String> providerTopics() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (providerConfigJson == null) {
                throw new IOException("Configuration file provider-topics.json not found");
            }
            Map<String, Object> jsonData = mapper.readValue(providerConfigJson, new TypeReference<>() {});
            return (Map<String, String>) jsonData.get("provider_topics");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load provider configuration: " + e.getMessage(), e);
        }
    }

    @Bean
    public int getTopicsCount() {
        return topicsCount;
    }

    @Bean
    public int[] getPercentages() {
        try {
            return Arrays.stream(percentages.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid percentages format: " + percentages);
        }
    }
}
