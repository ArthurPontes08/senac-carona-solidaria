package com.senac.carona.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitacoes_carona")
public class SolicitacaoCarona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carona_id", nullable = false)
    private Long caronaId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carona_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_solicitacoes_carona"))
    @SuppressWarnings("unused")
    private Carona carona;

    @Column(name = "passageiro_id", nullable = false)
    private Long passageiroId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passageiro_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_solicitacoes_passageiro"))
    @SuppressWarnings("unused")
    private Usuario passageiro;

    @Column(name = "passageiro_nome", nullable = false)
    private String passageiroNome;

    @Column(name = "ponto_embarque", nullable = false)
    private String pontoEmbarque;

    @Column(name = "mensagem", length = 140)
    private String mensagem;

    @Column(nullable = false)
    private String status; // PENDENTE, CONFIRMADA, RECUSADA, CANCELADA

    @Column(name = "telefone_motorista_liberado")
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

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTelefoneMotoristaLiberado() { return telefoneMotoristaLiberado; }
    public void setTelefoneMotoristaLiberado(String telefoneMotoristaLiberado) {
        this.telefoneMotoristaLiberado = telefoneMotoristaLiberado;
    }
}