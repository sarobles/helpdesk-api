package com.helpdesk.api.controller;

import com.helpdesk.api.dto.AscenderRolRequest;
import com.helpdesk.api.dto.UsuarioResponse;
import com.helpdesk.api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/soporte")
    public ResponseEntity<UsuarioResponse> ascenderASoporte(@RequestBody AscenderRolRequest request) {
        return ResponseEntity.ok(usuarioService.ascenderASoporte(request.email()));
    }
}

