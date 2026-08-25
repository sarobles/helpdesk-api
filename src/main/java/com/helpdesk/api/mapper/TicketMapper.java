package com.helpdesk.api.mapper;

import com.helpdesk.api.dto.TicketResponse;
import com.helpdesk.api.entity.EstadoTicket;
import com.helpdesk.api.entity.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket) {
        boolean vencido = ticket.getEstado() != EstadoTicket.RESUELTO
                && ticket.getSlaVenceEn().isBefore(LocalDateTime.now());

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getPrioridad(),
                ticket.getEstado(),
                ticket.getCreadoEn(),
                ticket.getSlaVenceEn(),
                vencido,
                ticket.getCreadoPor().getEmail()
        );
    }
}
