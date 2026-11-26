package com.senac.carona.dto;

import com.senac.carona.model.Usuario;

import java.util.Locale;

public class UsuarioResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String role;
    private final String region;

    private UsuarioResponse(Long id, String name, String email, String role, String region) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.region = region;
    }

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmailSenac(),
                mapRole(usuario.getPerfil()),
                usuario.getBairroOrigem()
        );
    }

    private static String mapRole(String perfil) {
        if (perfil == null) {
            return null;
        }
        return switch (perfil.trim().toUpperCase(Locale.ROOT)) {
            case "AMBOS" -> "both";
            case "OFERECE" -> "driver";
            case "BUSCA" -> "rider";
            default -> perfil;
        };
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getRegion() { return region; }
}
