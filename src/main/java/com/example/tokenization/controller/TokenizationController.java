package com.example.tokenization.controller;

import com.example.tokenization.service.FpeTokenizationService;
import com.example.tokenization.service.S3UploadService;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private S3UploadService s3UploadService;

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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename,
            @RequestParam("uploadUser") String uploadUser) {
        try {
            String key = s3UploadService.uploadFile(file, filename, uploadUser);
            return ResponseEntity.ok("File uploaded successfully with key: " + key);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}