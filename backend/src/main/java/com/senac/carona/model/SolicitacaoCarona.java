package com.senac.carona.model;

public class SolicitacaoCarona {

    private Long id;
    private Long caronaId;
    private Long passageiroId;
    private String passageiroNome;
    private String pontoEmbarque;
    private String status; // PENDENTE, CONFIRMADA, RECUSADA, CANCELADA
    private String telefoneMotoristaLiberado; // Só preenchido quando CONFIRMADA

    public SolicitacaoCarona() {
    }

    public SolicitacaoCarona(Long id, Long caronaId, Long passageiroId, String passageiroNome,
                             String pontoEmbarque, String status, String telefoneMotoristaLiberado) {
        this.id = id;
        this.caronaId = caronaId;
        this.passageiroId = passageiroId;
        this.passageiroNome = passageiroNome != null ? passageiroNome.trim() : null;
        this.pontoEmbarque = pontoEmbarque != null ? pontoEmbarque.trim() : null;
        this.status = status != null ? status.trim().toUpperCase() : null;
        this.telefoneMotoristaLiberado = telefoneMotoristaLiberado != null
                ? telefoneMotoristaLiberado.trim() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCaronaId() { return caronaId; }
    public void setCaronaId(Long caronaId) { this.caronaId = caronaId; }

    public Long getPassageiroId() { return passageiroId; }
    public void setPassageiroId(Long passageiroId) { this.passageiroId = passageiroId; }

    public String getPassageiroNome() { return passageiroNome; }
    public void setPassageiroNome(String passageiroNome) { this.passageiroNome = passageiroNome; }

    public String getPontoEmbarque() { return pontoEmbarque; }
    public void setPontoEmbarque(String pontoEmbarque) { this.pontoEmbarque = pontoEmbarque; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTelefoneMotoristaLiberado() { return telefoneMotoristaLiberado; }
    public void setTelefoneMotoristaLiberado(String telefoneMotoristaLiberado) {
        this.telefoneMotoristaLiberado = telefoneMotoristaLiberado;
    }
}