package com.helpdesk.api.repository;

import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método derivado básico
    Optional<Usuario> findByEmail(String email);
    
    // Método derivado con condición booleana
    List<Usuario> findByActivoTrue();
    
    // Método derivado con like
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    // JPQL con JOIN implícito
    @Query("SELECT u FROM Usuario u JOIN u.ticketsCreados t WHERE t.estado = 'ABIERTO'")
    List<Usuario> findUsuariosConTicketsAbiertos();
    
    // Método derivado con múltiples criterios
    List<Usuario> findByRolAndActivoTrue(Rol rol);
    
    // Método derivado con ordenamiento
    List<Usuario> findAllByOrderByFechaRegistroDesc();
    
    // JPQL con parámetros
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    List<Usuario> findUsuariosActivosPorRol(@Param("rol") Rol rol);
    
    // Consulta nativa
    @Query(value = "SELECT * FROM usuarios WHERE YEAR(fecha_registro) = :year", nativeQuery = true)
    List<Usuario> findUsuariosRegistradosEnAnio(@Param("year") int year);
}