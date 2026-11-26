package com.senac.carona.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.senac.carona.model.Carona;

public class CriarOfertaRequest {

    @JsonAlias("motoristaId")
    private Long driverId;

    private String origin;

    private String destination;

    @JsonAlias("data")
    private String date;

    @JsonAlias("horario")
    private String time;

    @JsonAlias("vagasTotais")
    private Integer seats;

    @JsonAlias("pontoEncontro")
    private String ref;

    @JsonAlias("observacao")
    private String notes;

    public Carona toCarona() {
        String destino = destination == null || destination.isBlank()
                ? "Campus Senac Canoas" : destination;
        return new Carona(null, driverId, null, null, origin, destino, date, time,
                ref, seats == null ? 0 : seats, null, notes);
    }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
