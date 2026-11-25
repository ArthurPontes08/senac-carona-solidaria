package com.senac.carona.dto;

import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;

import java.util.List;

public class MinhasCaronasResponse {

    private List<Carona> oferecidas;
    private List<SolicitacaoCarona> pedidas;

    public MinhasCaronasResponse(List<Carona> oferecidas, List<SolicitacaoCarona> pedidas) {
        this.oferecidas = oferecidas;
        this.pedidas = pedidas;
    }

    public List<Carona> getOferecidas() { return oferecidas; }
    public List<SolicitacaoCarona> getPedidas() { return pedidas; }
}