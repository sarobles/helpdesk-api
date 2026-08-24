package com.helpdesk.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long accessTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secreto,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs
    ) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
    }

    // Genera un AccessToken nuevo para un usuario ya autenticado.
    public String generarAccessToken(String email, String rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave)
                .compact();
    }

    // Extrae el email (subject) de un token ya validado.
    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    // Extrae el rol guardado como claim personalizado.
    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    // Verifica firma y expiracion. Si algo no cuadra, lanza una excepcion
    // (firma invalida, token expirado, formato corrupto, etc.) que
    // capturaremos en el filtro (Modulo 7, siguiente pedacito).
    public boolean esTokenValido(String token) {
        try {
            extraerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}