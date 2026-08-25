package com.helpdesk.api.dto;

import com.helpdesk.api.entity.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequest(
        @NotBlank(message = "El titulo es obligatorio")
        String titulo,

        @NotBlank(message = "La descripcion es obligatoria")
        String descripcion,

        @NotNull(message = "La prioridad es obligatoria")
        Prioridad prioridad
) {
}
