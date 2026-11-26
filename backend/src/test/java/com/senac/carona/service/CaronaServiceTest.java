package com.senac.carona.service;

import com.senac.carona.dto.MinhaContaSnapshotResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import com.senac.carona.repository.CaronaRepository;
import com.senac.carona.repository.SolicitacaoCaronaRepository;
import com.senac.carona.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CaronaServiceTest {

    private UsuarioRepository usuarioRepository;
    private CaronaRepository caronaRepository;
    private SolicitacaoCaronaRepository solicitacaoRepository;
    private CaronaService service;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        caronaRepository = mock(CaronaRepository.class);
        solicitacaoRepository = mock(SolicitacaoCaronaRepository.class);
        service = new CaronaService(usuarioRepository, caronaRepository, solicitacaoRepository);
    }

    @Test
    void naoPermiteEmailDuplicado() {
        String email = "arthur.pontes@senacrs.com.br";
        when(usuarioRepository.existsByEmailSenacIgnoreCase(email)).thenReturn(true);
        Usuario duplicado = usuario(null, "Outro Usuário", email, "BUSCA");

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(duplicado));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void persisteUsuarioSemReutilizarIdEnviadoPeloCliente() {
        Usuario novoUsuario = usuario(99L, "  Arthur Pontes  ",
                "ARTHUR.PONTES@SENACRS.COM.BR", "AMBOS");
        when(usuarioRepository.existsByEmailSenacIgnoreCase("arthur.pontes@senacrs.com.br"))
                .thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario salvo = invocation.getArgument(0);
            salvo.setId(7L);
            return salvo;
        });

        Usuario salvo = service.cadastrarUsuario(novoUsuario);

        assertEquals(7L, salvo.getId());
        assertEquals("Arthur Pontes", salvo.getNome());
        assertEquals("arthur.pontes@senacrs.com.br", salvo.getEmailSenac());
    }

    @Test
    void persisteUsuarioSemTelefoneQuandoCampoNaoInformado() {
        Usuario novoUsuario = new Usuario(null, "Arthur Pontes",
                "arthur.pontes@senacrs.com.br", "ALUNO", "AMBOS", "Centro", null);
        when(usuarioRepository.existsByEmailSenacIgnoreCase("arthur.pontes@senacrs.com.br"))
                .thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario salvo = service.cadastrarUsuario(novoUsuario);

        assertNull(salvo.getTelefone());
        verify(usuarioRepository).save(novoUsuario);
    }

    @Test
    void rejeitaTelefoneInformadoEmFormatoInvalido() {
        Usuario novoUsuario = new Usuario(null, "Arthur Pontes",
                "arthur.pontes@senacrs.com.br", "ALUNO", "AMBOS", "Centro", "12");
        when(usuarioRepository.existsByEmailSenacIgnoreCase("arthur.pontes@senacrs.com.br"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(novoUsuario));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void naoPermiteDataInvalidaAoPublicarCarona() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(1L,
                "Motorista", "motorista@senacrs.com.br", "OFERECE")));
        Carona carona = new Carona(null, 1L, null, null,
                "Mathias Velho", "Senac Canoas",
                "2026-99-99", "18:15", "Estação", 2, "ABERTA", "");

        assertThrows(IllegalArgumentException.class, () -> service.publicarCarona(carona));
        verify(caronaRepository, never()).save(any(Carona.class));
    }

    @Test
    void confirmarECancelarSolicitacaoMantemVagasConsistentes() {
        Carona carona = carona(1L, 1L, 3, 3, "ABERTA");
        SolicitacaoCarona solicitacao = solicitacao(10L, 1L, 3L, "PENDENTE");
        prepararSolicitacao(carona, solicitacao);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(
                usuario(1L, "Motorista", "motorista@senacrs.com.br", "OFERECE")));
        when(solicitacaoRepository.save(any(SolicitacaoCarona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(caronaRepository.save(any(Carona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SolicitacaoCarona confirmada = service.responderSolicitacao(10L, 1L, "CONFIRMAR");
        assertEquals("CONFIRMADA", confirmada.getStatus());
        assertEquals(2, carona.getVagasDisponiveis());

        SolicitacaoCarona cancelada = service.responderSolicitacao(10L, 1L, "CANCELAR");
        assertEquals("CANCELADA", cancelada.getStatus());
        assertEquals(3, carona.getVagasDisponiveis());
        verify(solicitacaoRepository, org.mockito.Mockito.times(2))
                .save(any(SolicitacaoCarona.class));
    }

    @Test
    void persisteSolicitacaoPendenteNoRepository() {
        Carona carona = carona(1L, 1L, 3, 3, "ABERTA");
        Usuario passageiro = usuario(3L, "Passageiro",
                "passageiro@senacrs.com.br", "BUSCA");
        when(caronaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(carona));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(passageiro));
        when(solicitacaoRepository.existsByCaronaIdAndPassageiroIdAndStatusIn(
                1L, 3L, List.of("PENDENTE", "CONFIRMADA"))).thenReturn(false);
        when(solicitacaoRepository.save(any(SolicitacaoCarona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SolicitacaoCarona criada = service.solicitarVaga(1L, 3L, null, "  Posso esperar aqui?  ");

        assertEquals("PENDENTE", criada.getStatus());
        assertEquals("Praça", criada.getPontoEmbarque());
        assertEquals("Posso esperar aqui?", criada.getMensagem());
        verify(solicitacaoRepository).save(any(SolicitacaoCarona.class));
    }

    @Test
    void naoPermiteMensagemDeSolicitacaoMaiorQue140Caracteres() {
        assertThrows(IllegalArgumentException.class,
                () -> service.solicitarVaga(1L, 3L, null, "x".repeat(141)));
        verify(solicitacaoRepository, never()).save(any(SolicitacaoCarona.class));
    }

    @Test
    void filtraOfertasPorHorarioVagasEStatus() {
        Carona tardeComVagas = carona(1L, 1L, 4, 3, "ABERTA");
        tardeComVagas.setHorario("20:00");
        Carona cedoComVagas = carona(2L, 1L, 4, 3, "ABERTA");
        cedoComVagas.setHorario("18:30");
        Carona lotada = carona(3L, 1L, 3, 0, "LOTADA");
        lotada.setHorario("20:30");
        when(caronaRepository.findAll()).thenReturn(List.of(tardeComVagas, cedoComVagas, lotada));

        List<Carona> ofertas = service.buscarOfertas(null, null, "19:00", 2);

        assertEquals(List.of(tardeComVagas), ofertas);
    }

    @Test
    void rejeitaFiltrosDeOfertaInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarOfertas(null, null, "25:00", 1));
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarOfertas(null, null, null, 0));
    }

    @Test
    void snapshotUsaFormatoDoFrontendEMarcaSolicitacoesProprias() {
        Usuario atual = usuario(1L, "Usuário atual", "atual@senacrs.com.br", "AMBOS");
        Usuario outroPassageiro = usuario(2L, "Outro passageiro",
                "passageiro@senacrs.com.br", "BUSCA");
        Carona oferecida = carona(10L, 1L, 3, 2, "ABERTA");
        Carona solicitada = carona(20L, 3L, 3, 2, "ABERTA");
        SolicitacaoCarona minhaSolicitacao = solicitacao(100L, 20L, 1L, "PENDENTE");
        SolicitacaoCarona recebida = solicitacao(101L, 10L, 2L, "PENDENTE");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(atual));
        when(usuarioRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(atual, outroPassageiro));
        when(caronaRepository.findByMotoristaId(1L)).thenReturn(List.of(oferecida));
        when(caronaRepository.findAllById(List.of(20L))).thenReturn(List.of(solicitada));
        when(solicitacaoRepository.findByPassageiroId(1L)).thenReturn(List.of(minhaSolicitacao));
        when(solicitacaoRepository.findByCaronaIdIn(List.of(10L))).thenReturn(List.of(recebida));

        MinhaContaSnapshotResponse snapshot = service.snapshot(1L);

        assertEquals(2, snapshot.getOffers().size());
        assertTrue(snapshot.getOffers().get(0).isMine());
        assertFalse(snapshot.getOffers().get(1).isMine());
        assertEquals(2, snapshot.getRequests().size());
        assertTrue(snapshot.getRequests().get(0).isMine());
        assertFalse(snapshot.getRequests().get(1).isMine());
        assertEquals("pending", snapshot.getRequests().get(0).getStatus());
    }

    @Test
    void naoPermiteMotoristaSolicitarNaPropriaCarona() {
        when(caronaRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(carona(1L, 1L, 3, 3, "ABERTA")));

        assertThrows(IllegalArgumentException.class,
                () -> service.solicitarVaga(1L, 1L, null));
    }

    @Test
    void naoPermiteConfirmacaoDeOutroUsuario() {
        Carona carona = carona(1L, 1L, 3, 3, "ABERTA");
        SolicitacaoCarona solicitacao = solicitacao(10L, 1L, 3L, "PENDENTE");
        prepararSolicitacao(carona, solicitacao);

        assertThrows(IllegalArgumentException.class,
                () -> service.responderSolicitacao(10L, 2L, "CONFIRMAR"));
    }

    @Test
    void naoPermiteConfirmacaoRepetida() {
        Carona carona = carona(1L, 1L, 3, 3, "ABERTA");
        SolicitacaoCarona solicitacao = solicitacao(10L, 1L, 3L, "PENDENTE");
        prepararSolicitacao(carona, solicitacao);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(
                usuario(1L, "Motorista", "motorista@senacrs.com.br", "OFERECE")));
        when(solicitacaoRepository.save(any(SolicitacaoCarona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(caronaRepository.save(any(Carona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        service.responderSolicitacao(10L, 1L, "CONFIRMAR");

        assertThrows(IllegalStateException.class,
                () -> service.responderSolicitacao(10L, 1L, "CONFIRMAR"));
    }

    @Test
    void cancelarCaronaCancelaSolicitacoesAtivas() {
        Carona carona = carona(1L, 1L, 3, 3, "ABERTA");
        SolicitacaoCarona solicitacao = solicitacao(10L, 1L, 3L, "PENDENTE");
        when(caronaRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(carona));
        when(caronaRepository.save(any(Carona.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(solicitacaoRepository.findByCaronaId(1L)).thenReturn(List.of(solicitacao));

        service.cancelarCarona(1L, 1L);

        assertEquals("CANCELADA", solicitacao.getStatus());
        assertEquals("CANCELADA", carona.getStatus());
        verify(solicitacaoRepository).saveAll(List.of(solicitacao));
    }

    private void prepararSolicitacao(Carona carona, SolicitacaoCarona solicitacao) {
        when(solicitacaoRepository.findCaronaIdById(solicitacao.getId()))
                .thenReturn(Optional.of(carona.getId()));
        when(caronaRepository.findByIdForUpdate(carona.getId())).thenReturn(Optional.of(carona));
        when(solicitacaoRepository.findByIdForUpdate(solicitacao.getId()))
                .thenReturn(Optional.of(solicitacao));
    }

    private Usuario usuario(Long id, String nome, String email, String perfil) {
        return new Usuario(id, nome, email, "ALUNO", perfil, "Centro", "(51) 90000-0000");
    }

    private Carona carona(Long id, Long motoristaId, int vagasTotais,
                          int vagasDisponiveis, String status) {
        Carona carona = new Carona(id, motoristaId, "Motorista", "ALUNO • Comunidade Senac",
                "Centro", "Senac Canoas", LocalDate.now().plusDays(2).toString(),
                "18:00", "Praça", vagasTotais, status, null);
        carona.setVagasDisponiveis(vagasDisponiveis);
        return carona;
    }

    private SolicitacaoCarona solicitacao(Long id, Long caronaId, Long passageiroId, String status) {
        return new SolicitacaoCarona(id, caronaId, passageiroId,
                "Passageiro", "Praça", status, null);
    }
}
