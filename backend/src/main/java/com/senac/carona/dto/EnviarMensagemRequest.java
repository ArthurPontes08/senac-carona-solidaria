package com.senac.carona.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class EnviarMensagemRequest {

    @JsonAlias("usuarioId")
    private Long userId;

    @JsonAlias("texto")
    private String text;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
