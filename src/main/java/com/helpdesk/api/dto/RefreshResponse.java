package com.helpdesk.api.dto;

public record RefreshResponse(
        String accessToken,
        String tipo
) {
    public RefreshResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
