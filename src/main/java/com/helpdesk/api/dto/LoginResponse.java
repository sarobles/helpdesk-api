package com.helpdesk.api.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tipo
) {
    public LoginResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}