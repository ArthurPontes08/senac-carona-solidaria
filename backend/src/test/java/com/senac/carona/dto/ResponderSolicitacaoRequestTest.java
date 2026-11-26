package com.senac.carona.dto;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponderSolicitacaoRequestTest {

    @Test
    void leAcaoEmInglesDoFrontendEIdDoUsuario() throws Exception {
        ResponderSolicitacaoRequest request = new ObjectMapper().readValue("""
                {
                  "userId": 7,
                  "action": "accept"
                }
                """, ResponderSolicitacaoRequest.class);

        assertEquals(7L, request.getUserId());
        assertEquals("accept", request.getAction());
        assertEquals("ACEITAR", request.toAcao());
    }

    @Test
    void aceitaNomesPortuguesesDoContratoExistente() throws Exception {
        ResponderSolicitacaoRequest request = new ObjectMapper().readValue("""
                {
                  "usuarioId": 7,
                  "acao": "CONFIRMAR"
                }
                """, ResponderSolicitacaoRequest.class);

        assertEquals(7L, request.getUserId());
        assertEquals("CONFIRMAR", request.getAction());
    }
}
