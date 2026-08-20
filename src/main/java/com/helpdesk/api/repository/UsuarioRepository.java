package com.helpdesk.api.repository;

import com.helpdesk.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Necesario para el login: buscar un usuario por su email.
    Optional<Usuario> findByEmail(String email);

    // Necesario para validar email duplicado en el registro (409 Conflict).
    boolean existsByEmail(String email);
}