package com.helpdesk.api.dto;

import com.helpdesk.api.entity.EstadoTicket;
import com.helpdesk.api.entity.Prioridad;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String titulo,
        String descripcion,
        Prioridad prioridad,
        EstadoTicket estado,
        LocalDateTime creadoEn,
        LocalDateTime slaVenceEn,
        boolean vencido,
        String creadoPorEmail
) {
}