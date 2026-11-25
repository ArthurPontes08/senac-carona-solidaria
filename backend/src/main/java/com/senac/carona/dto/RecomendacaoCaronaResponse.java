package com.senac.carona.dto;

import com.senac.carona.model.Carona;

public class RecomendacaoCaronaResponse {

    private Carona carona;
    private int scoreCompatibilidade;
    private boolean horarioApertado;
    private String resumo;

    public RecomendacaoCaronaResponse(Carona carona, int scoreCompatibilidade,
                                      boolean horarioApertado, String resumo) {
        this.carona = carona;
        this.scoreCompatibilidade = scoreCompatibilidade;
        this.horarioApertado = horarioApertado;
        this.resumo = resumo;
    }

    public Carona getCarona() { return carona; }
    public int getScoreCompatibilidade() { return scoreCompatibilidade; }
    public boolean isHorarioApertado() { return horarioApertado; }
    public String getResumo() { return resumo; }
}