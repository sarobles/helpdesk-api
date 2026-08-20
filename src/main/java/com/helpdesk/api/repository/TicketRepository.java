package com.helpdesk.api.repository;

import com.helpdesk.api.entity.EstadoTicket;
import com.helpdesk.api.entity.Ticket;
import com.helpdesk.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // GET /api/tickets/mios -> tickets creados por el usuario autenticado.
    List<Ticket> findByCreadoPor(Usuario creadoPor);

    // GET /api/tickets (rol SOPORTE/ADMIN) ya lo cubre findAll() heredado.

    // GET /api/tickets/vencidos -> tickets cuyo SLA ya paso y siguen sin resolver.
    @Query("SELECT t FROM Ticket t WHERE t.slaVenceEn < :ahora AND t.estado <> :estadoResuelto")
    List<Ticket> findVencidos(@Param("ahora") LocalDateTime ahora,
                               @Param("estadoResuelto") EstadoTicket estadoResuelto);
}