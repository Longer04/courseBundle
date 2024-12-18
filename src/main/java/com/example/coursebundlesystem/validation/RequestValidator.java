package com.example.coursebundlesystem.validation;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestValidator {

    public void validateTeacherRequest(Map<String, Map<String, Integer>> request) {
        if (request == null || !request.containsKey("topics")) {
            throw new IllegalArgumentException("Request must contain a 'topics' key with topic data.");
        }

        Map<String, Integer> topics = request.get("topics");

        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException("'topics' must contain at least one topic with a non-null value.");
        }

        for (Map.Entry<String, Integer> entry : topics.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank()) {
                throw new IllegalArgumentException("Topic names cannot be null or blank.");
            }
            if (entry.getValue() == null || entry.getValue() <= 0) {
                throw new IllegalArgumentException("Topic values must be greater than 0.");
            }
        }
    }
}
