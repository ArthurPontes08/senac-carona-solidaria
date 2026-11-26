package com.senac.carona.controller;

import com.senac.carona.model.Reserva;
import com.senac.carona.model.StatusReserva;
import com.senac.carona.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/solicitar")
    public ResponseEntity<Reserva> solicitarReserva(@RequestParam Long caronaId, @RequestParam Long passageiroId) {
        Reserva novaReserva = reservaService.solicitarReserva(caronaId, passageiroId);
        return ResponseEntity.ok(novaReserva);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Reserva> atualizarStatus(@PathVariable Long id, @RequestParam StatusReserva novoStatus) {
        Reserva reservaAtualizada = reservaService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(reservaAtualizada);
    }
}