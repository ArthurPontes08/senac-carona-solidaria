package com.senac.carona.repository;

import com.senac.carona.model.SolicitacaoCarona;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoCaronaRepository extends JpaRepository<SolicitacaoCarona, Long> {

    List<SolicitacaoCarona> findByCaronaId(Long caronaId);

    List<SolicitacaoCarona> findByCaronaIdIn(Collection<Long> caronaIds);

    List<SolicitacaoCarona> findByPassageiroId(Long passageiroId);

    boolean existsByCaronaIdAndPassageiroIdAndStatusIn(
            Long caronaId, Long passageiroId, Collection<String> statuses);

    @Query("select s.caronaId from SolicitacaoCarona s where s.id = :solicitacaoId")
    Optional<Long> findCaronaIdById(@Param("solicitacaoId") Long solicitacaoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SolicitacaoCarona s where s.id = :id")
    Optional<SolicitacaoCarona> findByIdForUpdate(@Param("id") Long id);
}
