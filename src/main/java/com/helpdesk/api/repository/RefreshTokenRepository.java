package com.helpdesk.api.repository;

import com.helpdesk.api.entity.RefreshToken;
import com.helpdesk.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Usado en /api/auth/refresh para validar el token recibido.
    Optional<RefreshToken> findByToken(String token);

    // Util para invalidar todas las sesiones activas de un usuario si hiciera falta.
    void deleteByUsuario(Usuario usuario);
}