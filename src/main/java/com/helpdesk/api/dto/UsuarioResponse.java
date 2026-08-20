package com.helpdesk.api.dto;

import com.helpdesk.api.entity.Rol;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        Rol rol
) {
}