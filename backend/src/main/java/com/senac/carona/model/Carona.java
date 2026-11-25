package com.senac.carona.model;

public class Carona {

    private Long id;
    private Long motoristaId;
    private String motoristaNome;
    private String vinculoSenac; // Ex: "ALUNO • Comunidade Senac"
    private String origem;
    private String destino;
    private String data;         // Ex: "2026-11-25"
    private String horario;      // Ex: "18:15"
    private String pontoEncontro;
    private int vagasTotais;
    private int vagasDisponiveis;
    private String status;       // ABERTA, LOTADA, CANCELADA
    private String observacao;

    public Carona() {
    }

    public Carona(Long id, Long motoristaId, String motoristaNome, String vinculoSenac,
                  String origem, String destino, String data, String horario,
                  String pontoEncontro, int vagasTotais, String status, String observacao) {
        this.id = id;
        this.motoristaId = motoristaId;
        this.motoristaNome = motoristaNome != null ? motoristaNome.trim() : null;
        this.vinculoSenac = vinculoSenac != null ? vinculoSenac.trim() : null;
        this.origem = origem != null ? origem.trim() : null;
        this.destino = destino != null ? destino.trim() : null;
        this.data = data != null ? data.trim() : null;
        this.horario = horario != null ? horario.trim() : null;
        this.pontoEncontro = pontoEncontro != null ? pontoEncontro.trim() : null;
        this.vagasTotais = vagasTotais;
        this.vagasDisponiveis = vagasTotais;
        this.status = status != null ? status.trim().toUpperCase() : "ABERTA";
        this.observacao = observacao != null ? observacao.trim() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMotoristaId() { return motoristaId; }
    public void setMotoristaId(Long motoristaId) { this.motoristaId = motoristaId; }

    public String getMotoristaNome() { return motoristaNome; }
    public void setMotoristaNome(String motoristaNome) { this.motoristaNome = motoristaNome; }

    public String getVinculoSenac() { return vinculoSenac; }
    public void setVinculoSenac(String vinculoSenac) { this.vinculoSenac = vinculoSenac; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getPontoEncontro() { return pontoEncontro; }
    public void setPontoEncontro(String pontoEncontro) { this.pontoEncontro = pontoEncontro; }

    public int getVagasTotais() { return vagasTotais; }
    public void setVagasTotais(int vagasTotais) { this.vagasTotais = vagasTotais; }

    public int getVagasDisponiveis() { return vagasDisponiveis; }
    public void setVagasDisponiveis(int vagasDisponiveis) { this.vagasDisponiveis = vagasDisponiveis; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}