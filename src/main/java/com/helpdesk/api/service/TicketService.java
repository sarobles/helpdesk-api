package com.helpdesk.api.service;

import com.helpdesk.api.dto.EstadoUpdateRequest;
import com.helpdesk.api.dto.TicketRequest;
import com.helpdesk.api.dto.TicketResponse;
import com.helpdesk.api.entity.EstadoTicket;
import com.helpdesk.api.entity.Prioridad;
import com.helpdesk.api.entity.Ticket;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.exception.AccesoDenegadoTicketException;
import com.helpdesk.api.exception.TicketNoEncontradoException;
import com.helpdesk.api.mapper.TicketMapper;
import com.helpdesk.api.repository.TicketRepository;
import com.helpdesk.api.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository,
                          UsuarioRepository usuarioRepository,
                          TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public TicketResponse crear(TicketRequest request, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow();

        LocalDateTime ahora = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .prioridad(request.prioridad())
                .estado(EstadoTicket.ABIERTO)
                .creadoEn(ahora)
                .slaVenceEn(calcularSlaVenceEn(request.prioridad(), ahora))
                .creadoPor(usuario)
                .build();

        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> listarMios(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow();

        return ticketRepository.findByCreadoPor(usuario).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public List<TicketResponse> listarTodos() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public List<TicketResponse> listarVencidos() {
        return ticketRepository.findVencidos(LocalDateTime.now(), EstadoTicket.RESUELTO).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public TicketResponse obtenerPorId(Long id, Authentication authentication) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNoEncontradoException(id));

        if (!esPropietarioORolPrivilegiado(ticket, authentication)) {
            throw new AccesoDenegadoTicketException();
        }

        return ticketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse cambiarEstado(Long id, EstadoUpdateRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNoEncontradoException(id));

        ticket.setEstado(request.estado());
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    private boolean esPropietarioORolPrivilegiado(Ticket ticket, Authentication authentication) {
        boolean esPropietario = ticket.getCreadoPor().getEmail().equalsIgnoreCase(authentication.getName());
        boolean tieneRolPrivilegiado = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SOPORTE") || a.getAuthority().equals("ROLE_ADMIN"));
        return esPropietario || tieneRolPrivilegiado;
    }

    private LocalDateTime calcularSlaVenceEn(Prioridad prioridad, LocalDateTime creadoEn) {
        return switch (prioridad) {
            case ALTA -> creadoEn.plusHours(4);
            case MEDIA -> creadoEn.plusHours(24);
            case BAJA -> creadoEn.plusHours(72);
        };
    }
}
