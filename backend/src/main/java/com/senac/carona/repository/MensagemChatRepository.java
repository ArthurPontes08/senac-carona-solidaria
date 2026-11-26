package com.senac.carona.repository;

import com.senac.carona.model.MensagemChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemChatRepository extends JpaRepository<MensagemChat, Long> {

    List<MensagemChat> findBySolicitacaoIdOrderByCriadoEmAscIdAsc(Long solicitacaoId);

    void deleteBySolicitacaoId(Long solicitacaoId);
}
