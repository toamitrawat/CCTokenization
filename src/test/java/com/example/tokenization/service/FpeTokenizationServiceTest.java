package com.example.tokenization.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FpeTokenizationServiceTest {
    private final FpeTokenizationService service = new FpeTokenizationService();

    @Test
    void testTokenizeAndDetokenize_Valid16Digit() {
        String ccNumber = "1234567890123456";
        String token = service.tokenize(ccNumber);
        assertNotNull(token);
        assertEquals(16, token.length());
        assertNotEquals(ccNumber, token);
        String detokenized = service.detokenize(token);
        assertEquals(ccNumber, detokenized);
    }

    @Test
    void testTokenize_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.tokenize("1234"));
        assertThrows(IllegalArgumentException.class, () -> service.tokenize("abcd567890123456"));
        assertThrows(IllegalArgumentException.class, () -> service.tokenize(null));
    }

    @Test
    void testDetokenize_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.detokenize("1234"));
        assertThrows(IllegalArgumentException.class, () -> service.detokenize("abcd567890123456"));
        assertThrows(IllegalArgumentException.class, () -> service.detokenize(null));
    }
}
