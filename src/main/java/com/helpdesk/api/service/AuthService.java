package com.helpdesk.api.service;

import com.helpdesk.api.dto.RegistroRequest;
import com.helpdesk.api.dto.UsuarioResponse;
import com.helpdesk.api.entity.Rol;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.exception.EmailYaRegistradoException;
import com.helpdesk.api.mapper.UsuarioMapper;
import com.helpdesk.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public AuthService(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailYaRegistradoException(request.email());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(Rol.USUARIO)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioGuardado);
    }
}