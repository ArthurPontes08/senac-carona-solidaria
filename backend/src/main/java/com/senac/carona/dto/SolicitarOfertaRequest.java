package com.senac.carona.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class SolicitarOfertaRequest {

    @JsonAlias("passageiroId")
    private Long passengerId;

    @JsonAlias("mensagem")
    private String msg;

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
}
