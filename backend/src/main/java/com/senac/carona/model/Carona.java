package com.senac.carona.model;

import jakarta.persistence.*;

@Entity
@Table(name = "caronas")
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String motorista;

    @Column(name = "telefone_contato")
    private String telefoneContato;

    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String destino;

    @Column(name = "data_hora")
    private String dataHora;

    @Column(name = "vagas_totais")
    private int vagasTotais;

    @Column(name = "vagas_disponiveis")
    private int vagasDisponiveis;

    @Column
    private String observacao;

    public Carona() {
    }

    public Carona(Long id, String motorista, String telefoneContato, String origem,
                  String destino, String dataHora, int vagasTotais, String observacao) {
        this.id = id;
        this.motorista = motorista;
        this.telefoneContato = telefoneContato;
        this.origem = origem;
        this.destino = destino;
        this.dataHora = dataHora;
        this.vagasTotais = vagasTotais;
        this.vagasDisponiveis = vagasTotais;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
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

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}