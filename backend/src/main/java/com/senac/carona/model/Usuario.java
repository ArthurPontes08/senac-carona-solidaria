package com.senac.carona.model;

public class Usuario {

    private Long id;
    private String nome;
    private String emailSenac;
    private String tipoVinculo; // ALUNO ou FUNCIONARIO
    private String perfil;      // OFERECE, BUSCA ou AMBOS
    private String bairroOrigem;
    private String telefone;

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

    public String getTipoVinculo() { return tipoVinculo; }
    public void setTipoVinculo(String tipoVinculo) { this.tipoVinculo = tipoVinculo; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getBairroOrigem() { return bairroOrigem; }
    public void setBairroOrigem(String bairroOrigem) { this.bairroOrigem = bairroOrigem; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}