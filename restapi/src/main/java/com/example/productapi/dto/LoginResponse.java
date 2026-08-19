package com.example.productapi.dto;


/**
 * Represents the authentication response containing an access token.
 */
public class LoginResponse {

    private String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}