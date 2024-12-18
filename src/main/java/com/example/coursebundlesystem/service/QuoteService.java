package com.example.coursebundlesystem.service;

import java.util.Map;

public interface QuoteService {
    Map<String, Double> calculateQuotes(Map<String, Map<String, Integer>> teacherRequest);
}
