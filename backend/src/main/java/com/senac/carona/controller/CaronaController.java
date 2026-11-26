package com.senac.carona.controller;

import com.senac.carona.dto.AtualizarPerfilRequest;
import com.senac.carona.dto.CadastroUsuarioRequest;
import com.senac.carona.dto.CriarOfertaRequest;
import com.senac.carona.dto.MinhaContaSnapshotResponse;
import com.senac.carona.dto.MinhasCaronasResponse;
import com.senac.carona.dto.OfertaResponse;
import com.senac.carona.dto.RecomendacaoCaronaResponse;
import com.senac.carona.dto.ResponderSolicitacaoRequest;
import com.senac.carona.dto.SolicitarOfertaRequest;
import com.senac.carona.dto.SolicitacaoResponse;
import com.senac.carona.dto.UsuarioResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import com.senac.carona.service.CaronaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CaronaController {

    private final CaronaService caronaService;

    public CaronaController(CaronaService caronaService) {
        this.caronaService = caronaService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(
            @RequestBody CadastroUsuarioRequest request) {
        Usuario salvo = caronaService.cadastrarUsuario(request.toUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(salvo));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = caronaService.listarUsuarios().stream()
                .map(UsuarioResponse::from)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> atualizarPerfil(
            @PathVariable Long id, @RequestBody AtualizarPerfilRequest request) {
        Usuario atualizado = caronaService.atualizarPerfil(id, request.getName(), request.getPhoto());
        return ResponseEntity.ok(UsuarioResponse.from(atualizado));
    }

    @GetMapping("/usuarios/{id}/minhas-caronas")
    public ResponseEntity<MinhasCaronasResponse> minhasCaronas(@PathVariable Long id) {
        return ResponseEntity.ok(caronaService.minhasCaronas(id));
    }

    @GetMapping("/minhas-caronas")
    public ResponseEntity<MinhaContaSnapshotResponse> snapshot(
            @RequestParam Long userId) {
        return ResponseEntity.ok(caronaService.snapshot(userId));
    }

    @GetMapping("/caronas")
    public ResponseEntity<List<Carona>> listarCaronas(
            @RequestParam(required = false) String data,
            @RequestParam(required = false) String regiao) {
        return ResponseEntity.ok(caronaService.buscarCaronas(data, regiao));
    }

    @PostMapping("/caronas")
    public ResponseEntity<Carona> publicarCarona(@RequestBody Carona carona) {
        Carona criada = caronaService.publicarCarona(carona);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<OfertaResponse>> listarOfertas(
            @RequestParam(required = false) String data,
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String horario,
            @RequestParam(required = false) Integer vagas,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long userId) {
        List<Carona> ofertas = caronaService.buscarOfertas(data, regiao, horario, vagas, q, userId);
        Map<Long, Integer> compatibilidade = caronaService
                .recomendarCaronas(data, regiao, horario).stream()
                .collect(Collectors.toMap(
                        recomendacao -> recomendacao.getCarona().getId(),
                        RecomendacaoCaronaResponse::getScoreCompatibilidade));
        List<OfertaResponse> resposta = ofertas.stream()
                .map(oferta -> OfertaResponse.from(
                        oferta, compatibilidade.getOrDefault(oferta.getId(), 50)))
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/ofertas")
    public ResponseEntity<OfertaResponse> publicarOferta(
            @RequestBody CriarOfertaRequest request) {
        Carona criada = caronaService.publicarCarona(request.toCarona());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OfertaResponse.from(criada, 100, true));
    }

    @PostMapping("/ofertas/{id}/solicitacoes")
    public ResponseEntity<SolicitacaoResponse> solicitarOferta(
            @PathVariable Long id, @RequestBody SolicitarOfertaRequest request) {
        Long passageiroId = request.getPassengerId();
        SolicitacaoCarona criada = caronaService.solicitarVaga(
                id, passageiroId, null, request.getMsg());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respostaSolicitacao(criada, passageiroId));
    }

    @GetMapping("/caronas/recomendadas")
    public ResponseEntity<List<RecomendacaoCaronaResponse>> recomendarCaronas(
            @RequestParam(required = false) String data,
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String horarioDesejado) {
        return ResponseEntity.ok(caronaService.recomendarCaronas(data, regiao, horarioDesejado));
    }

    @GetMapping("/caronas/{id}")
    public ResponseEntity<Carona> buscarCarona(@PathVariable Long id) {
        return caronaService.buscarCaronaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/caronas/{id}/solicitar")
    public ResponseEntity<SolicitacaoCarona> solicitarVaga(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        if (body == null || !body.containsKey("passageiroId") || body.get("passageiroId") == null) {
            throw new IllegalArgumentException("Informe passageiroId no corpo da requisição.");
        }
        Long passageiroId;
        try {
            passageiroId = Long.valueOf(body.get("passageiroId").toString().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Informe passageiroId válido no corpo da requisição.");
        }
        Object pontoEmbarqueObjeto = body.get("pontoEmbarque");
        String pontoEmbarque = pontoEmbarqueObjeto == null
                ? null : pontoEmbarqueObjeto.toString();
        SolicitacaoCarona sol = caronaService.solicitarVaga(id, passageiroId, pontoEmbarque);
        return ResponseEntity.status(HttpStatus.CREATED).body(sol);
    }

    @GetMapping("/caronas/{id}/solicitacoes")
    public ResponseEntity<List<SolicitacaoResponse>> listarSolicitacoes(
            @PathVariable Long id, @RequestParam Long userId) {
        Carona carona = caronaService.buscarCaronaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        List<SolicitacaoResponse> solicitacoes = caronaService
                .listarSolicitacoesPorCarona(id, userId).stream()
                .map(solicitacao -> {
                    Usuario passageiro = caronaService.buscarUsuarioPorId(solicitacao.getPassageiroId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "Passageiro vinculado à solicitação não encontrado."));
                    return SolicitacaoResponse.from(solicitacao, carona, passageiro, false);
                })
                .toList();
        return ResponseEntity.ok(solicitacoes);
    }

    @PatchMapping("/solicitacoes/{id}/responder")
    public ResponseEntity<SolicitacaoCarona> responderSolicitacao(
            @PathVariable Long id,
            @RequestParam String acao,
            @RequestParam Long motoristaId) {
        SolicitacaoCarona atualizada = caronaService.responderSolicitacao(id, motoristaId, acao);
        return ResponseEntity.ok(atualizada);
    }

    @PatchMapping("/solicitacoes/{id}")
    public ResponseEntity<SolicitacaoResponse> responderSolicitacao(
            @PathVariable Long id, @RequestBody ResponderSolicitacaoRequest request) {
        SolicitacaoCarona atualizada = caronaService.responderSolicitacao(
                id, request.getUserId(), request.toAcao());
        return ResponseEntity.ok(respostaSolicitacao(atualizada, request.getUserId()));
    }

    @PatchMapping("/caronas/{id}/cancelar")
    public ResponseEntity<Carona> cancelarCarona(
            @PathVariable Long id,
            @RequestParam Long motoristaId) {
        return ResponseEntity.ok(caronaService.cancelarCarona(id, motoristaId));
    }

    private SolicitacaoResponse respostaSolicitacao(
            SolicitacaoCarona solicitacao, Long userId) {
        Carona carona = caronaService.buscarCaronaPorId(solicitacao.getCaronaId())
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        Usuario passageiro = caronaService.buscarUsuarioPorId(solicitacao.getPassageiroId())
                .orElseThrow(() -> new IllegalArgumentException("Passageiro não encontrado."));
        return SolicitacaoResponse.from(
                solicitacao, carona, passageiro,
                userId.equals(solicitacao.getPassageiroId()));
    }
}