package com.example.tokenization.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FpeTokenizationService {
    private static final Logger logger = LoggerFactory.getLogger(FpeTokenizationService.class);
    private static final int PRESERVE = 4;
    private static final int OFFSET = 7; // Secret offset for digit shifting

    public String tokenize(String ccNumber) {
        try {
            if (ccNumber == null || ccNumber.length() != 16 || !ccNumber.matches("\\d{16}")) {
                logger.warn("Tokenization failed: invalid input length or format");
                throw new IllegalArgumentException("Credit card number must be 16 digits");
            }
            String maskedInput = mask(ccNumber);
            logger.info("Tokenizing credit card: {}", maskedInput);
            String toTransform = ccNumber.substring(0, 16 - PRESERVE);
            String preserved = ccNumber.substring(16 - PRESERVE);
            StringBuilder tokenized = new StringBuilder();
            for (char c : toTransform.toCharArray()) {
                int digit = c - '0';
                int shifted = (digit + OFFSET) % 10;
                tokenized.append(shifted);
            }
            String token = tokenized.toString() + preserved;
            logger.info("Tokenization result: {}", mask(token));
            return token;
        } catch (Exception e) {
            logger.error("Error during tokenization: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String detokenize(String token) {
        try {
            if (token == null || token.length() != 16 || !token.matches("\\d{16}")) {
                logger.warn("Detokenization failed: invalid input length or format");
                throw new IllegalArgumentException("Token must be 16 digits");
            }
            logger.info("Detokenizing credit card token: {}", mask(token));
            String toReverse = token.substring(0, 16 - PRESERVE);
            String preserved = token.substring(16 - PRESERVE);
            StringBuilder detokenized = new StringBuilder();
            for (char c : toReverse.toCharArray()) {
                int digit = c - '0';
                int original = (digit - OFFSET + 10) % 10;
                detokenized.append(original);
            }
            String ccNumber = detokenized.toString() + preserved;
            logger.info("Detokenization result: {}", mask(ccNumber));
            return ccNumber;
        } catch (Exception e) {
            logger.error("Error during detokenization: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String mask(String value) {
        if (value == null || value.length() < 4) return "****";
        int unmasked = 4;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length() - unmasked; i++) sb.append('*');
        sb.append(value.substring(value.length() - unmasked));
        return sb.toString();
    }
} 