package com.helpdesk.api.exception;

public class AccesoDenegadoTicketException extends RuntimeException {
    public AccesoDenegadoTicketException() {
        super("No tienes permiso para ver este ticket");
    }
}
