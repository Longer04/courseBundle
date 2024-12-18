package com.example.coursebundlesystem.service;

import com.example.coursebundlesystem.calculation.DoubleMatchStrategy;
import com.example.coursebundlesystem.calculation.QuoteCalculationStrategy;
import com.example.coursebundlesystem.calculation.SingleMatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuoteServiceImpl implements QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final Map<String, String> providerTopics;
    private final int topicsCount;
    private final int[] percentages;

    private final QuoteCalculationStrategy singleMatchStrategy;
    private final QuoteCalculationStrategy doubleMatchStrategy;

    @Autowired
    public QuoteServiceImpl(Map<String, String> providerTopics, int topicsCount, @Qualifier("getPercentages") int[] percentages) {
        this.providerTopics = providerTopics;
        this.topicsCount = topicsCount;
        this.percentages = percentages;
        this.singleMatchStrategy = new SingleMatchStrategy();
        this.doubleMatchStrategy = new DoubleMatchStrategy();
    }

    @Override
    public Map<String, Double> calculateQuotes(Map<String, Map<String, Integer>> teacherRequest) {
        logger.info("Starting quote calculation...");

        validateProviderTopics();
        Map<String, Integer> topics = extractTopicsFromRequest(teacherRequest);
        List<Map.Entry<String, Integer>> topTopics = getTopTopics(topics, topicsCount);
        Map<String, Integer> topicImportance = calculateTopicImportance(topTopics);

        return calculateQuotesForProviders(topTopics, topicImportance);
    }

    private void validateProviderTopics() {
        if (providerTopics == null || providerTopics.isEmpty()) {
            logger.error("Provider topics configuration is missing or invalid");
            throw new RuntimeException("Provider topics configuration is missing or invalid");
        }
    }

    private Map<String, Integer> extractTopicsFromRequest(Map<String, Map<String, Integer>> teacherRequest) {
        if (teacherRequest == null || teacherRequest.get("topics") == null || teacherRequest.get("topics").isEmpty()) {
            logger.error("Invalid or empty topics in request");
            throw new RuntimeException("Invalid or empty topics in request");
        }
        return teacherRequest.get("topics");
    }

    private Map<String, Integer> calculateTopicImportance(List<Map.Entry<String, Integer>> topTopics) {
        Map<String, Integer> topicImportance = new LinkedHashMap<>();
        if (percentages.length < topTopics.size()) {
            logger.warn("Percentages array is shorter than topTopics size. Using default values for remaining topics.");
        }

        for (int i = 0; i < topTopics.size(); i++) {
            int importance = (i < percentages.length) ? percentages[i] : 30;
            topicImportance.put(topTopics.get(i).getKey(), importance);
        }

        logger.debug("Topic importance map: {}", topicImportance);
        return topicImportance;
    }

    private Map<String, Double> calculateQuotesForProviders(
            List<Map.Entry<String, Integer>> topTopics, Map<String, Integer> topicImportance) {

        Map<String, Double> quotes = new HashMap<>();
        for (Map.Entry<String, String> provider : providerTopics.entrySet()) {
            logger.debug("Calculating quote for provider: {}", provider.getKey());
            double quote = calculateProviderQuote(provider, topTopics, topicImportance);

            if (quote > 0) {
                quotes.put(provider.getKey(), quote);
                logger.info("Quote calculated for provider {}: {}", provider.getKey(), quote);
            }
        }
        logger.info("Final quotes: {}", quotes);
        return quotes;
    }

    private double calculateProviderQuote(
            Map.Entry<String, String> provider,
            List<Map.Entry<String, Integer>> topTopics,
            Map<String, Integer> topicImportance) {

        double doubleMatchQuote = doubleMatchStrategy.calculate(provider, topTopics, topicImportance);
        if (doubleMatchQuote > 0) {
            logger.debug("Double match strategy result for provider {}: {}", provider.getKey(), doubleMatchQuote);
            return doubleMatchQuote;
        }

        double singleMatchQuote = singleMatchStrategy.calculate(provider, topTopics, topicImportance);
        logger.debug("Single match strategy result for provider {}: {}", provider.getKey(), singleMatchQuote);
        return singleMatchQuote;
    }

    private List<Map.Entry<String, Integer>> getTopTopics(Map<String, Integer> topics, int limit) {
        return topics.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}