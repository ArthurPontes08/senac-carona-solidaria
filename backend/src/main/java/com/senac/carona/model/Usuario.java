package com.senac.carona.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "email_senac", nullable = false, unique = true)
    private String emailSenac;

    @Column(name = "senha_hash", length = 255)
    private String senhaHash;

    @Column(name = "tipo_vinculo")
    private String tipoVinculo; // ALUNO ou FUNCIONARIO

    @Column
    private String perfil;      // OFERECE, BUSCA ou AMBOS

    @Column(name = "bairro_origem")
    private String bairroOrigem;

    @Column
    private String telefone;

    @Column(name = "foto_perfil", columnDefinition = "TEXT")
    private String fotoPerfil;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String emailSenac, String tipoVinculo,
                   String perfil, String bairroOrigem, String telefone) {
        this.id = id;
        this.nome = nome != null ? nome.trim() : null;
        this.emailSenac = emailSenac != null ? emailSenac.trim().toLowerCase() : null;
        this.tipoVinculo = tipoVinculo != null ? tipoVinculo.trim().toUpperCase() : null;
        this.perfil = perfil != null ? perfil.trim().toUpperCase() : null;
        this.bairroOrigem = bairroOrigem != null ? bairroOrigem.trim() : null;
        this.telefone = telefone != null ? telefone.trim() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmailSenac() { return emailSenac; }
    public void setEmailSenac(String emailSenac) { this.emailSenac = emailSenac; }

    @JsonIgnore
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTipoVinculo() { return tipoVinculo; }
    public void setTipoVinculo(String tipoVinculo) { this.tipoVinculo = tipoVinculo; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getBairroOrigem() { return bairroOrigem; }
    public void setBairroOrigem(String bairroOrigem) { this.bairroOrigem = bairroOrigem; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}