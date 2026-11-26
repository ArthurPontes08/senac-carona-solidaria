package com.senac.carona.dto;

import com.senac.carona.model.Carona;

public class OfertaResponse {

    private final Long id;
    private final String driver;
    private final String origin;
    private final String destination;
    private final String date;
    private final String time;
    private final int seats;
    private final String ref;
    private final String summary;
    private final int match;

    private OfertaResponse(Carona carona, int match) {
        this.id = carona.getId();
        this.driver = carona.getMotoristaNome();
        this.origin = carona.getOrigem();
        this.destination = carona.getDestino();
        this.date = carona.getData();
        this.time = carona.getHorario();
        this.seats = carona.getVagasDisponiveis();
        this.ref = carona.getPontoEncontro();
        this.summary = carona.getOrigem() + " → " + carona.getDestino();
        this.match = match;
    }

    public static OfertaResponse from(Carona carona, int match) {
        return new OfertaResponse(carona, match);
    }

    public Long getId() { return id; }
    public String getDriver() { return driver; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getSeats() { return seats; }
    public String getRef() { return ref; }
    public String getSummary() { return summary; }
    public int getMatch() { return match; }
}
