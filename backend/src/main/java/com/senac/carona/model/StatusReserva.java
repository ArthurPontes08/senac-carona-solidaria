package com.senac.carona.model;
import jakarta.persistence.*;

public enum StatusReserva {
    PENDENTE,
    CONFIRMADA,
    RECUSADA,
    CANCELADA
}

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carona_id", nullable = false)
    private Carona carona;

    @ManyToOne
    @JoinColumn(name = "passageiro_id", nullable = false)
    private Usuario passageiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.PENDENTE;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Carona getCarona() { return carona; }
    public void setCarona(Carona carona) { this.carona = carona; }
    public Usuario getPassageiro() { return passageiro; }
    public void setPassageiro(Usuario passageiro) { this.passageiro = passageiro; }
    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }
}