package com.senac.carona.controller;

import com.senac.carona.dto.CadastroUsuarioRequest;
import com.senac.carona.dto.MinhasCaronasResponse;
import com.senac.carona.dto.RecomendacaoCaronaResponse;
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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
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

    @GetMapping("/usuarios/{id}/minhas-caronas")
    public ResponseEntity<MinhasCaronasResponse> minhasCaronas(@PathVariable Long id) {
        return ResponseEntity.ok(caronaService.minhasCaronas(id));
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
    public ResponseEntity<List<SolicitacaoCarona>> listarSolicitacoes(@PathVariable Long id) {
        return ResponseEntity.ok(caronaService.listarSolicitacoesPorCarona(id));
    }

    @PatchMapping("/solicitacoes/{id}/responder")
    public ResponseEntity<SolicitacaoCarona> responderSolicitacao(
            @PathVariable Long id,
            @RequestParam String acao,
            @RequestParam(required = false) Long motoristaId) {
        SolicitacaoCarona atualizada = caronaService.responderSolicitacao(id, motoristaId, acao);
        return ResponseEntity.ok(atualizada);
    }

    @PatchMapping("/caronas/{id}/cancelar")
    public ResponseEntity<Carona> cancelarCarona(
            @PathVariable Long id,
            @RequestParam Long motoristaId) {
        return ResponseEntity.ok(caronaService.cancelarCarona(id, motoristaId));
    }
}