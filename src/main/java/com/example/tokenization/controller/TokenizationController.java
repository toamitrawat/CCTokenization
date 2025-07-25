package com.example.tokenization.controller;

import com.example.tokenization.service.FpeTokenizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tokenization.dto.CreditCardRequest;
import com.example.tokenization.dto.TokenResponse;
import com.example.tokenization.dto.TokenRequest;
import com.example.tokenization.dto.CreditCardResponse;

@RestController
@RequestMapping("/api")
public class TokenizationController {
    @Autowired
    private FpeTokenizationService fpeTokenizationService;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenResponse> tokenize(@RequestBody CreditCardRequest request) {
        String token = fpeTokenizationService.tokenize(request.getCcNumber());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/detokenize")
    public ResponseEntity<CreditCardResponse> detokenize(@RequestBody TokenRequest request) {
        String ccNumber = fpeTokenizationService.detokenize(request.getToken());
        return ResponseEntity.ok(new CreditCardResponse(ccNumber));
    }
} 