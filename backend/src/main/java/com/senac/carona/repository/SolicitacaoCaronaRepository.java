package com.senac.carona.repository;

import com.senac.carona.model.SolicitacaoCarona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SolicitacaoCaronaRepository extends JpaRepository<SolicitacaoCarona, Long> {

    List<SolicitacaoCarona> findByCaronaId(Long caronaId);

    List<SolicitacaoCarona> findByPassageiroId(Long passageiroId);

    boolean existsByCaronaIdAndPassageiroIdAndStatusIn(
            Long caronaId, Long passageiroId, Collection<String> statuses);
}
