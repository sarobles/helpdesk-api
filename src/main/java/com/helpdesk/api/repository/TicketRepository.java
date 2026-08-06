package com.helpdesk.api.repository;

import com.helpdesk.api.entity.Ticket;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.entity.EstadoTicket;
import com.helpdesk.api.entity.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Método derivado - Spring interpreta el nombre
    List<Ticket> findByEstado(EstadoTicket estado);
    
    // Método derivado con múltiples condiciones
    List<Ticket> findByPrioridadAndEstado(Prioridad prioridad, EstadoTicket estado);
    
    // Método derivado con ordenamiento
    List<Ticket> findByUsuarioAsignadoOrderByFechaCreacionDesc(Usuario usuario);
    
    // JPQL - Consulta personalizada con JOIN
    @Query("SELECT t FROM Ticket t JOIN t.usuarioCreador u WHERE u.email = :email")
    List<Ticket> findTicketsByCreadorEmail(@Param("email") String email);
    
    // JPQL con condiciones complejas
    @Query("SELECT t FROM Ticket t WHERE t.fechaCreacion BETWEEN :fechaInicio AND :fechaFin AND t.estado = :estado")
    List<Ticket> findTicketsByFechaRangoYEstado(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("estado") EstadoTicket estado);
    
    // Consulta nativa - SQL puro
    @Query(value = "SELECT * FROM tickets t WHERE t.tiempo_resolucion > :limite", nativeQuery = true)
    List<Ticket> findTicketsConResolucionMayorA(@Param("limite") Integer limite);
    
    // Método para verificar existencia
    boolean existsByTitulo(String titulo);
    
    // Método para contar
    long countByEstado(EstadoTicket estado);
    
    // Optional - para manejar ausencia de datos
    Optional<Ticket> findByIdAndUsuarioCreador(Long id, Usuario usuario);
}