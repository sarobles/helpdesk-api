package com.helpdesk.api.exception;

public class RefreshTokenInvalidoException extends RuntimeException {

    public RefreshTokenInvalidoException() {
        super("Refresh token inválido, expirado o revocado");
    }
}