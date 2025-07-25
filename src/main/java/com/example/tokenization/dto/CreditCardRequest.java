package com.example.tokenization.dto;

public class CreditCardRequest {
    private String ccNumber;
    public CreditCardRequest() {}
    public CreditCardRequest(String ccNumber) { this.ccNumber = ccNumber; }
    public String getCcNumber() { return ccNumber; }
    public void setCcNumber(String ccNumber) { this.ccNumber = ccNumber; }
} 