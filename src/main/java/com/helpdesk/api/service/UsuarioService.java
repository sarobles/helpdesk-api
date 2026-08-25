package com.helpdesk.api.service;

import com.helpdesk.api.dto.UsuarioResponse;
import com.helpdesk.api.entity.Rol;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.exception.UsuarioNoEncontradoException;
import com.helpdesk.api.mapper.UsuarioMapper;
import com.helpdesk.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioResponse ascenderASoporte(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNoEncontradoException::new);

        usuario.setRol(Rol.SOPORTE);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
}
