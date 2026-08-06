package com.helpdesk.api.repository;

import com.helpdesk.api.entity.RefreshToken;
import com.helpdesk.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByTokenAndRevocadoFalse(String token);
    
    void deleteByUsuario(Usuario usuario);
    
    void deleteAllByFechaExpiracionBefore(LocalDateTime fecha);
}