package com.example.tokenization.dto;

public class CreditCardResponse {
    private String ccNumber;
    public CreditCardResponse() {}
    public CreditCardResponse(String ccNumber) { this.ccNumber = ccNumber; }
    public String getCcNumber() { return ccNumber; }
    public void setCcNumber(String ccNumber) { this.ccNumber = ccNumber; }
} 