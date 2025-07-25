package com.example.tokenization.controller;

import com.example.tokenization.service.FpeTokenizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TokenizationController {
    @Autowired
    private FpeTokenizationService fpeTokenizationService;

    @PostMapping("/tokenize")
    public ResponseEntity<Map<String, String>> tokenize(@RequestBody Map<String, String> request) {
        String ccNumber = request.get("ccNumber");
        String token = fpeTokenizationService.tokenize(ccNumber);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/detokenize")
    public ResponseEntity<Map<String, String>> detokenize(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String ccNumber = fpeTokenizationService.detokenize(token);
        return ResponseEntity.ok(Map.of("ccNumber", ccNumber));
    }
} 