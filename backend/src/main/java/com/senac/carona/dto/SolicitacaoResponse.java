package com.senac.carona.dto;

import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;

import java.util.Locale;

public class SolicitacaoResponse {

    private final Long id;
    private final Long offerId;
    private final String who;
    private final String region;
    private final String date;
    private final String time;
    private final String msg;
    private final String status;
    private final boolean mine;

    private SolicitacaoResponse(SolicitacaoCarona solicitacao, Carona carona,
                                Usuario passageiro, boolean mine) {
        this.id = solicitacao.getId();
        this.offerId = solicitacao.getCaronaId();
        this.who = passageiro.getNome();
        this.region = passageiro.getBairroOrigem();
        this.date = carona.getData();
        this.time = carona.getHorario();
        this.msg = solicitacao.getMensagem();
        this.status = mapStatus(solicitacao.getStatus());
        this.mine = mine;
    }

    public static SolicitacaoResponse from(SolicitacaoCarona solicitacao, Carona carona,
                                           Usuario passageiro, boolean mine) {
        return new SolicitacaoResponse(solicitacao, carona, passageiro, mine);
    }

    private static String mapStatus(String status) {
        if (status == null) {
            return null;
        }
        return switch (status.trim().toUpperCase(Locale.ROOT)) {
            case "PENDENTE" -> "pending";
            case "CONFIRMADA" -> "confirmed";
            case "RECUSADA" -> "refused";
            case "CANCELADA" -> "cancelled";
            default -> status;
        };
    }

    public Long getId() { return id; }
    public Long getOfferId() { return offerId; }
    public String getWho() { return who; }
    public String getRegion() { return region; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getMsg() { return msg; }
    public String getStatus() { return status; }
    public boolean isMine() { return mine; }
}
