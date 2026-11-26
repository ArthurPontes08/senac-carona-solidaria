package com.senac.carona.dto;

import java.util.List;

public class MinhaContaSnapshotResponse {

    private final List<OfertaResponse> offers;
    private final List<SolicitacaoResponse> requests;

    public MinhaContaSnapshotResponse(List<OfertaResponse> offers,
                                      List<SolicitacaoResponse> requests) {
        this.offers = List.copyOf(offers);
        this.requests = List.copyOf(requests);
    }

    public List<OfertaResponse> getOffers() { return offers; }
    public List<SolicitacaoResponse> getRequests() { return requests; }
}
