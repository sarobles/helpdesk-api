package com.helpdesk.api.service;

import com.helpdesk.api.dto.*;
import com.helpdesk.api.entity.RefreshToken;
import com.helpdesk.api.entity.Rol;
import com.helpdesk.api.entity.Usuario;
import com.helpdesk.api.exception.CredencialesInvalidasException;
import com.helpdesk.api.exception.EmailYaRegistradoException;
import com.helpdesk.api.exception.RefreshTokenInvalidoException;
import com.helpdesk.api.mapper.UsuarioMapper;
import com.helpdesk.api.repository.RefreshTokenRepository;
import com.helpdesk.api.repository.UsuarioRepository;
import com.helpdesk.api.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final JwtService jwtService;
    private final long refreshTokenExpirationMs;

    public AuthService(UsuarioRepository usuarioRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        PasswordEncoder passwordEncoder,
                        UsuarioMapper usuarioMapper,
                        JwtService jwtService,
                        @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.jwtService = jwtService;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
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
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }

        String accessToken = jwtService.generarAccessToken(usuario.getEmail(), usuario.getRol().name());
        String refreshTokenValue = generarRefreshTokenParaUsuario(usuario);

        return new LoginResponse(accessToken, refreshTokenValue);
    }

    @Transactional
    public RefreshResponse refrescarAccessToken(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(RefreshTokenInvalidoException::new);

        if (refreshToken.isRevocado() || refreshToken.getExpiraEn().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenInvalidoException();
        }

        Usuario usuario = refreshToken.getUsuario();
        String nuevoAccessToken = jwtService.generarAccessToken(usuario.getEmail(), usuario.getRol().name());

        return new RefreshResponse(nuevoAccessToken);
    }

    @Transactional
    public void logout(RefreshRequest request) {
        refreshTokenRepository.findByToken(request.refreshToken())
                .ifPresent(refreshToken -> {
                    refreshToken.setRevocado(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

    private String generarRefreshTokenParaUsuario(Usuario usuario) {
        String valor = UUID.randomUUID().toString() + UUID.randomUUID();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(valor)
                .usuario(usuario)
                .expiraEn(LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000))
                .revocado(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return valor;
    }
}