package com.helpdesk.api.dto;

public record RegistroRequest(
        String nombre,
        String email,
        String password
) {
}