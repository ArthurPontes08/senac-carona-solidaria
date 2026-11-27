package com.senac.carona.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.senac.carona.model.Usuario;

import java.util.Locale;

public class CadastroUsuarioRequest {

    @JsonAlias("nome")
    private String name;

    @JsonAlias("emailSenac")
    private String email;

    private String password;

    @JsonAlias("perfil")
    private String role;

    @JsonAlias("bairroOrigem")
    private String region;

    @JsonAlias("telefone")
    private String phone;

    @JsonAlias("tipoVinculo")
    private String tipoVinculo = "ALUNO";

    public Usuario toUsuario() {
        return new Usuario(null, name, email, tipoVinculo,
                mapPerfil(role), region, phone);
    }

    private String mapPerfil(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "both" -> "AMBOS";
            case "driver" -> "OFERECE";
            case "rider" -> "BUSCA";
            default -> value.trim().toUpperCase(Locale.ROOT);
        };
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setTipoVinculo(String tipoVinculo) { this.tipoVinculo = tipoVinculo; }
}
