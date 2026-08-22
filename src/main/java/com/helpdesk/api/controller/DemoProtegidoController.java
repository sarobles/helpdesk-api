package com.helpdesk.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller TEMPORAL, solo para el Modulo 6.
 * Sirve para comprobar que las rutas protegidas devuelven 401
 * antes de que exista JWT. Se elimina cuando tengamos TicketController real.
 */
@RestController
public class DemoProtegidoController {

    @GetMapping("/api/demo/protegido")
    public String protegido() {
        return "Si ves esto, ya estabas autenticado.";
    }
}