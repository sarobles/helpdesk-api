package com.helpdesk.api.controller;

import com.helpdesk.api.dto.EstadoUpdateRequest;
import com.helpdesk.api.dto.TicketRequest;
import com.helpdesk.api.dto.TicketResponse;
import com.helpdesk.api.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> crear(@Valid @RequestBody TicketRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.crear(request, authentication));
    }

    @GetMapping("/mios")
    public ResponseEntity<List<TicketResponse>> listarMios(Authentication authentication) {
        return ResponseEntity.ok(ticketService.listarMios(authentication));
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<TicketResponse>> listarVencidos() {
        return ResponseEntity.ok(ticketService.listarVencidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> obtener(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(ticketService.obtenerPorId(id, authentication));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listarTodos() {
        return ResponseEntity.ok(ticketService.listarTodos());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketResponse> cambiarEstado(@PathVariable Long id, @RequestBody EstadoUpdateRequest request) {
        return ResponseEntity.ok(ticketService.cambiarEstado(id, request));
    }
}