package com.example.coursebundlesystem.web;

import com.example.coursebundlesystem.service.QuoteService;
import com.example.coursebundlesystem.validation.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bundles")
public class CourseBundleController {

    private static final Logger logger = LoggerFactory.getLogger(CourseBundleController.class);
    private final QuoteService quoteService;
    private final RequestValidator validator;

    @Autowired
    public CourseBundleController(QuoteService quoteService, RequestValidator validator) {
        this.quoteService = quoteService;
        this.validator = validator;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateBundle(@RequestBody Map<String, Map<String, Integer>> teacherRequest) {
        logger.info("Received bundle generation request: {}", teacherRequest);
        validator.validateTeacherRequest(teacherRequest);
        Map<String, Double> result = quoteService.calculateQuotes(teacherRequest);
        logger.info("Generated bundle response: {}", result);
        return ResponseEntity.ok(result);
    }
}
