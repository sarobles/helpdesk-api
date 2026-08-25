package com.helpdesk.api.dto;

import com.helpdesk.api.entity.EstadoTicket;

public record EstadoUpdateRequest(
        EstadoTicket estado
) {
}