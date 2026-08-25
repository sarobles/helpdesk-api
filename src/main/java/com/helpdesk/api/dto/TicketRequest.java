package com.helpdesk.api.dto;

import com.helpdesk.api.entity.Prioridad;

public record TicketRequest(
        String titulo,
        String descripcion,
        Prioridad prioridad
) {
}