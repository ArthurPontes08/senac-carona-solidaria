package com.senac.carona.service;

import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CaronaServiceTest {

    @Test
    void naoPermiteEmailDuplicado() {
        CaronaService service = new CaronaService();
        Usuario duplicado = new Usuario(null, "Outro Usuário",
                "arthur.pontes@senacrs.com.br", "ALUNO", "BUSCA",
                "Centro", "(51) 90000-0000");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(duplicado));
    }

    @Test
    void naoPermiteDataInvalidaAoPublicarCarona() {
        CaronaService service = new CaronaService();
        Carona carona = new Carona(null, 1L, null, null,
                "Mathias Velho", "Senac Canoas",
                "2026-99-99", "18:15", "Estação", 2, "ABERTA", "");

        assertThrows(IllegalArgumentException.class, () -> service.publicarCarona(carona));
    }

    @Test
    void confirmarERestornarVagaMantemStatusConsistente() {
        CaronaService service = new CaronaService();

        SolicitacaoCarona solicitacao = service.solicitarVaga(1L, 3L, null);
        SolicitacaoCarona confirmada = service.responderSolicitacao(solicitacao.getId(), "CONFIRMAR");

        assertEquals("CONFIRMADA", confirmada.getStatus());
        assertEquals(2, service.buscarCaronaPorId(1L).orElseThrow().getVagasDisponiveis());

        SolicitacaoCarona cancelada = service.responderSolicitacao(solicitacao.getId(), "CANCELAR");

        assertEquals("CANCELADA", cancelada.getStatus());
        assertEquals(3, service.buscarCaronaPorId(1L).orElseThrow().getVagasDisponiveis());
    }

    @Test
    void naoPermiteMotoristaSolicitarNaPropriaCarona() {
        CaronaService service = new CaronaService();

        assertThrows(IllegalArgumentException.class,
                () -> service.solicitarVaga(1L, 1L, null));
    }
}
