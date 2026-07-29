package com.example.API.de.Mesa.de.Ayuda.model;

import java.beans.Transient;
import java.time.LocalDateTime;

import com.example.API.de.Mesa.de.Ayuda.model.enums.EstadoTicket;
import com.example.API.de.Mesa.de.Ayuda.model.enums.Prioridad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoTicket estado = EstadoTicket.ABIERTO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(nullable = false)
    private LocalDateTime slaVenceEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id", nullable = false)
    private Usuario creadoPor;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
        // estado ya tiene default ABIERTO gracias a @Builder.Default
    }

    // Campo calculado, NO se persiste en la base de datos
    @Transient
    public boolean isVencido() {
        return this.estado != EstadoTicket.RESUELTO
                && LocalDateTime.now().isAfter(this.slaVenceEn);
    }
}