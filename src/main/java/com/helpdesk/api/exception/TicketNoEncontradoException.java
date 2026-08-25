package com.helpdesk.api.exception;

public class TicketNoEncontradoException extends RuntimeException {
    public TicketNoEncontradoException(Long id) {
        super("Ticket no encontrado: " + id);
    }
}