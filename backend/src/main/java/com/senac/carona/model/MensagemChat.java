package com.senac.carona.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "mensagens_chat", indexes = {
        @Index(name = "idx_mensagens_chat_solicitacao_data",
                columnList = "solicitacao_id, criado_em")
})
public class MensagemChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solicitacao_id", nullable = false)
    private Long solicitacaoId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitacao_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_mensagens_chat_solicitacao"))
    private SolicitacaoCarona solicitacao;

    @Column(name = "remetente_id", nullable = false)
    private Long remetenteId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_mensagens_chat_remetente"))
    private Usuario remetente;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    public MensagemChat() {
    }

    public MensagemChat(Long solicitacaoId, Long remetenteId, String texto, Instant criadoEm) {
        this.solicitacaoId = solicitacaoId;
        this.remetenteId = remetenteId;
        this.texto = texto;
        this.criadoEm = criadoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSolicitacaoId() { return solicitacaoId; }
    public Long getRemetenteId() { return remetenteId; }
    public String getTexto() { return texto; }
    public Instant getCriadoEm() { return criadoEm; }
}
