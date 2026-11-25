package com.senac.carona.controller;

import com.senac.carona.dto.MinhasCaronasResponse;
import com.senac.carona.dto.RecomendacaoCaronaResponse;
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
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario salvo = caronaService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(caronaService.listarUsuarios());
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
    public ResponseEntity<?> publicarCarona(@RequestBody Carona carona) {
        try {
            Carona criada = caronaService.publicarCarona(carona);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
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
    public ResponseEntity<?> solicitarVaga(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        try {
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/caronas/{id}/solicitacoes")
    public ResponseEntity<List<SolicitacaoCarona>> listarSolicitacoes(@PathVariable Long id) {
        return ResponseEntity.ok(caronaService.listarSolicitacoesPorCarona(id));
    }

    @PatchMapping("/solicitacoes/{id}/responder")
    public ResponseEntity<?> responderSolicitacao(
            @PathVariable Long id,
            @RequestParam String acao) {
        try {
            SolicitacaoCarona atualizada = caronaService.responderSolicitacao(id, acao);
            return ResponseEntity.ok(atualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", e.getMessage()));
        }
    }
}