package com.senac.carona.service;

import com.senac.carona.model.Carona;
import com.senac.carona.model.Reserva;
import com.senac.carona.model.StatusReserva;
import com.senac.carona.model.Usuario;
import com.senac.carona.repository.CaronaRepository;
import com.senac.carona.repository.ReservaRepository;
import com.senac.carona.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CaronaRepository caronaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Reserva solicitarReserva(Long caronaId, Long passageiroId) {
        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new RuntimeException("Carona não encontrada"));
        Usuario passageiro = usuarioRepository.findById(passageiroId)
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado"));

        Reserva reserva = new Reserva();
        reserva.setCarona(carona);
        reserva.setPassageiro(passageiro);
        reserva.setStatus(StatusReserva.PENDENTE);

        return reservaRepository.save(reserva);
    }

    public Reserva atualizarStatus(Long reservaId, StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
        reserva.setStatus(novoStatus);
        return reservaRepository.save(reserva);
    }
}