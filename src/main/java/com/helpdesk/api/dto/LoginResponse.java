package com.helpdesk.api.dto;

public record LoginResponse(
        String accessToken,
        String tipo
) {
    public LoginResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}