package com.senac.carona.dto;

import com.senac.carona.model.Carona;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfertaApiDtoTest {

    @Test
    void convertePayloadDeOfertaDoFrontendECompletaDestinoFixo() throws Exception {
        CriarOfertaRequest request = new ObjectMapper().readValue("""
                {
                  "driverId": 7,
                  "origin": "Centro",
                  "date": "2027-01-15",
                  "time": "18:30",
                  "seats": 2,
                  "ref": "Praça central",
                  "notes": "Saída pontual"
                }
                """, CriarOfertaRequest.class);

        Carona carona = request.toCarona();

        assertEquals(7L, carona.getMotoristaId());
        assertEquals("Centro", carona.getOrigem());
        assertEquals("Campus Senac Canoas", carona.getDestino());
        assertEquals("2027-01-15", carona.getData());
        assertEquals("18:30", carona.getHorario());
        assertEquals(2, carona.getVagasTotais());
        assertEquals("Praça central", carona.getPontoEncontro());
        assertEquals("Saída pontual", carona.getObservacao());
    }

    @Test
    void respostaDaOfertaUsaCamposEmInglesEIncluiCompatibilidade() throws Exception {
        Carona carona = new Carona(11L, 7L, "Joana", "ALUNO",
                "Centro", "Campus Senac Canoas", "2027-01-15",
                "18:30", "Praça central", 3, "ABERTA", null);
        carona.setVagasDisponiveis(2);
        String json = new ObjectMapper().writeValueAsString(OfertaResponse.from(carona, 92));

        assertTrue(json.contains("\"driver\":\"Joana\""));
        assertTrue(json.contains("\"seats\":2"));
        assertTrue(json.contains("\"ref\":\"Praça central\""));
        assertTrue(json.contains("\"match\":92"));
    }
}
