package com.example.tokenization.controller;

import com.example.tokenization.service.FpeTokenizationService;
import com.example.tokenization.service.S3UploadService;
import org.springframework.web.multipart.MultipartFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(TokenizationController.class);
    @Autowired
    private FpeTokenizationService fpeTokenizationService;

    @Autowired
    private S3UploadService s3UploadService;

    @PostMapping("/tokenize")
    public ResponseEntity<TokenResponse> tokenize(@RequestBody CreditCardRequest request) {
        logger.info("Tokenize request received for card ending with: {}", request.getCcNumber().substring(Math.max(0, request.getCcNumber().length() - 4)));
        String token = fpeTokenizationService.tokenize(request.getCcNumber());
        logger.info("Token generated successfully");
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/detokenize")
    public ResponseEntity<CreditCardResponse> detokenize(@RequestBody TokenRequest request) {
        logger.info("Detokenize request received for token: {}", request.getToken());
        String ccNumber = fpeTokenizationService.detokenize(request.getToken());
        logger.info("Detokenization successful");
        return ResponseEntity.ok(new CreditCardResponse(ccNumber));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename,
            @RequestParam("uploadUser") String uploadUser) {
        logger.info("Upload request received: user={}, filename={}", uploadUser, filename);
        try {
            String key = s3UploadService.uploadFile(file, filename, uploadUser);
            logger.info("File uploaded successfully: key={}", key);
            return ResponseEntity.ok("File uploaded successfully with key: " + key);
        } catch (Exception e) {
            logger.error("File upload failed: user={}, filename={}, error={}", uploadUser, filename, e.getMessage(), e);
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}