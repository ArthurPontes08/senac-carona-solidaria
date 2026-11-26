package com.senac.carona.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Locale;

public class ResponderSolicitacaoRequest {

    @JsonAlias("usuarioId")
    private Long userId;

    @JsonAlias("acao")
    private String action;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String toAcao() {
        if (action == null) {
            return null;
        }
        return switch (action.trim().toLowerCase(Locale.ROOT)) {
            case "accept" -> "ACEITAR";
            case "refuse" -> "RECUSAR";
            case "cancel" -> "CANCELAR";
            default -> action;
        };
    }
}
