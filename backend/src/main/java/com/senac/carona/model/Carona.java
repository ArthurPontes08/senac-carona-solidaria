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
@Table(name = "caronas")
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motorista_id", nullable = false)
    private Long motoristaId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_caronas_motorista"))
    private Usuario motorista;

    @Column(name = "motorista_nome")
    private String motoristaNome;

    @Column(name = "vinculo_senac")
    private String vinculoSenac;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private String data;

    @Column(nullable = false)
    private String horario;

    @Column(name = "ponto_encontro")
    private String pontoEncontro;

    @Column(name = "vagas_totais")
    private int vagasTotais;

    @Column(name = "vagas_disponiveis")
    private int vagasDisponiveis;

    @Column
    private String status;

    @Column
    private String observacao;

    public Carona() {
    }

    public Carona(Long id, Long motoristaId, String motoristaNome, String vinculoSenac,
                  String origem, String destino, String data, String horario,
                  String pontoEncontro, int vagasTotais, String status, String observacao) {
        this.id = id;
        this.motoristaId = motoristaId;
        this.motoristaNome = motoristaNome;
        this.vinculoSenac = vinculoSenac;
        this.origem = origem;
        this.destino = destino;
        this.data = data;
        this.horario = horario;
        this.pontoEncontro = pontoEncontro;
        this.vagasTotais = vagasTotais;
        this.vagasDisponiveis = vagasTotais;
        this.status = status;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(Long motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getMotoristaNome() {
        return motoristaNome;
    }

    public void setMotoristaNome(String motoristaNome) {
        this.motoristaNome = motoristaNome;
    }

    public String getVinculoSenac() {
        return vinculoSenac;
    }

    public void setVinculoSenac(String vinculoSenac) {
        this.vinculoSenac = vinculoSenac;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getPontoEncontro() {
        return pontoEncontro;
    }

    public void setPontoEncontro(String pontoEncontro) {
        this.pontoEncontro = pontoEncontro;
    }

    public int getVagasTotais() {
        return vagasTotais;
    }

    public void setVagasTotais(int vagasTotais) {
        this.vagasTotais = vagasTotais;
    }

    public int getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}