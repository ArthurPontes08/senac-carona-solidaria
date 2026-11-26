package com.senac.carona.service;

import com.senac.carona.dto.ChatMessageResponse;
import com.senac.carona.dto.ConversaResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.MensagemChat;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import com.senac.carona.repository.CaronaRepository;
import com.senac.carona.repository.MensagemChatRepository;
import com.senac.carona.repository.SolicitacaoCaronaRepository;
import com.senac.carona.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    private UsuarioRepository usuarioRepository;
    private CaronaRepository caronaRepository;
    private SolicitacaoCaronaRepository solicitacaoRepository;
    private MensagemChatRepository mensagemRepository;
    private ChatService service;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        caronaRepository = mock(CaronaRepository.class);
        solicitacaoRepository = mock(SolicitacaoCaronaRepository.class);
        mensagemRepository = mock(MensagemChatRepository.class);
        service = new ChatService(usuarioRepository, caronaRepository,
                solicitacaoRepository, mensagemRepository);
    }

    @Test
    void enviaMensagemPersistidaParaParticipanteConfirmado() {
        prepararConversa(12L, "CONFIRMADA");
        when(mensagemRepository.save(any(MensagemChat.class))).thenAnswer(invocation -> {
            MensagemChat mensagem = invocation.getArgument(0);
            mensagem.setId(30L);
            return mensagem;
        });

        ChatMessageResponse response = service.enviarMensagem(20L, 12L, "  Até amanhã!  ");

        assertEquals(30L, response.getId());
        assertEquals("Passageiro", response.getSender());
        assertEquals("Até amanhã!", response.getText());
        assertTrue(response.isMine());
        verify(mensagemRepository).save(any(MensagemChat.class));
    }

    @Test
    void listaConversaEmOrdemEIdentificaMensagensDoUsuarioAtual() {
        prepararConversa(12L, "CONFIRMADA");
        MensagemChat doMotorista = new MensagemChat(20L, 7L, "Olá",
                Instant.parse("2026-11-26T12:00:00Z"));
        doMotorista.setId(1L);
        MensagemChat doPassageiro = new MensagemChat(20L, 12L, "Oi",
                Instant.parse("2026-11-26T12:01:00Z"));
        doPassageiro.setId(2L);
        when(mensagemRepository.findBySolicitacaoIdOrderByCriadoEmAscIdAsc(20L))
                .thenReturn(List.of(doMotorista, doPassageiro));

        ConversaResponse response = service.buscarConversa(20L, 12L);

        assertEquals(20L, response.getRequestId());
        assertEquals("Motorista", response.getPartner());
        assertEquals(2, response.getMessages().size());
        assertFalse(response.getMessages().get(0).isMine());
        assertTrue(response.getMessages().get(1).isMine());
    }

    @Test
    void bloqueiaConversaSemSolicitacaoConfirmada() {
        prepararConversa(12L, "PENDENTE");

        assertThrows(IllegalStateException.class,
                () -> service.buscarConversa(20L, 12L));
        verify(mensagemRepository, never())
                .findBySolicitacaoIdOrderByCriadoEmAscIdAsc(20L);
    }

    @Test
    void bloqueiaUsuarioQueNaoParticipaDaSolicitacao() {
        when(solicitacaoRepository.findById(20L))
                .thenReturn(Optional.of(solicitacao("CONFIRMADA")));
        when(caronaRepository.findById(10L)).thenReturn(Optional.of(carona()));

        assertThrows(IllegalArgumentException.class,
                () -> service.buscarConversa(20L, 99L));
    }

    @Test
    void apagaMensagensApenasParaParticipanteDeConversaConfirmada() {
        prepararConversa(12L, "CONFIRMADA");

        service.apagarConversa(20L, 12L);

        verify(mensagemRepository).deleteBySolicitacaoId(20L);
    }

    @Test
    void impedeTerceiroDeApagarConversa() {
        when(solicitacaoRepository.findById(20L))
                .thenReturn(Optional.of(solicitacao("CONFIRMADA")));
        when(caronaRepository.findById(10L)).thenReturn(Optional.of(carona()));

        assertThrows(IllegalArgumentException.class,
                () -> service.apagarConversa(20L, 99L));
        verify(mensagemRepository, never()).deleteBySolicitacaoId(20L);
    }

    @Test
    void rejeitaMensagemVaziaOuMaiorQue500Caracteres() {
        assertThrows(IllegalArgumentException.class,
                () -> service.enviarMensagem(20L, 12L, " "));
        assertThrows(IllegalArgumentException.class,
                () -> service.enviarMensagem(20L, 12L, "x".repeat(501)));
        verify(mensagemRepository, never()).save(any(MensagemChat.class));
    }

    private void prepararConversa(Long userId, String status) {
        when(solicitacaoRepository.findById(20L))
                .thenReturn(Optional.of(solicitacao(status)));
        when(caronaRepository.findById(10L)).thenReturn(Optional.of(carona()));
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(
                user(userId, userId == 7L ? "Motorista" : "Passageiro")));
        Long partnerId = userId == 7L ? 12L : 7L;
        when(usuarioRepository.findById(partnerId)).thenReturn(Optional.of(
                user(partnerId, partnerId == 7L ? "Motorista" : "Passageiro")));
    }

    private SolicitacaoCarona solicitacao(String status) {
        return new SolicitacaoCarona(20L, 10L, 12L, "Passageiro",
                "Praça", status, null);
    }

    private Carona carona() {
        return new Carona(10L, 7L, "Motorista", "ALUNO",
                "Centro", "Campus Senac Canoas", LocalDate.now().plusDays(2).toString(),
                "18:30", "Praça", 3, "ABERTA", null);
    }

    private Usuario user(Long id, String nome) {
        return new Usuario(id, nome, "pessoa@senacrs.com.br",
                "ALUNO", "AMBOS", "Centro", null);
    }
}
