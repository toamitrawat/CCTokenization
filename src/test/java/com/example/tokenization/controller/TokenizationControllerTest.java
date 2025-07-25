package com.example.tokenization.controller;

import com.example.tokenization.dto.CreditCardRequest;
import com.example.tokenization.dto.TokenRequest;
import com.example.tokenization.service.FpeTokenizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenizationController.class)
class TokenizationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FpeTokenizationService fpeTokenizationService;

    private String ccNumber;
    private String token;

    @BeforeEach
    void setUp() {
        ccNumber = "1234567890123456";
        token = "8901234567893456";
    }

    @Test
    void testTokenizeEndpoint() throws Exception {
        when(fpeTokenizationService.tokenize(ccNumber)).thenReturn(token);
        String json = "{\"ccNumber\":\"" + ccNumber + "\"}";
        mockMvc.perform(post("/api/tokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void testDetokenizeEndpoint() throws Exception {
        when(fpeTokenizationService.detokenize(token)).thenReturn(ccNumber);
        String json = "{\"token\":\"" + token + "\"}";
        mockMvc.perform(post("/api/detokenize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ccNumber").value(ccNumber));
    }
}
