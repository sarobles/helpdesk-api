package com.helpdesk.api.dto;

public record LoginRequest(
        String email,
        String password
) {
}
