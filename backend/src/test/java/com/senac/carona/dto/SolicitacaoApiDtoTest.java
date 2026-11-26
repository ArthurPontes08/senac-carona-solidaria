package com.senac.carona.dto;

import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitacaoApiDtoTest {

    @Test
    void convertePayloadDeSolicitacaoComMensagem() throws Exception {
        SolicitarOfertaRequest request = new ObjectMapper().readValue("""
                {
                  "passengerId": 12,
                  "msg": "Posso esperar na praça?"
                }
                """, SolicitarOfertaRequest.class);

        assertEquals(12L, request.getPassengerId());
        assertEquals("Posso esperar na praça?", request.getMsg());
    }

    @Test
    void serializaSolicitacaoNoFormatoEsperadoPeloFrontend() throws Exception {
        SolicitacaoCarona solicitacao = new SolicitacaoCarona(4L, 8L, 12L,
                "Joana", "Praça", "PENDENTE", null);
        solicitacao.setMensagem("Posso esperar na praça?");
        Carona carona = new Carona(8L, 7L, "Motorista", "ALUNO",
                "Centro", "Campus Senac Canoas", "2027-01-15",
                "18:30", "Praça", 3, "ABERTA", null);
        Usuario passageiro = new Usuario(12L, "Joana", "joana@senacrs.edu.br",
                "ALUNO", "BUSCA", "Centro", null);

        String json = new ObjectMapper().writeValueAsString(
                SolicitacaoResponse.from(solicitacao, carona, passageiro, true));

        assertTrue(json.contains("\"offerId\":8"));
        assertTrue(json.contains("\"who\":\"Joana\""));
        assertTrue(json.contains("\"msg\":\"Posso esperar na praça?\""));
        assertTrue(json.contains("\"status\":\"pending\""));
        assertTrue(json.contains("\"mine\":true"));
    }
}
