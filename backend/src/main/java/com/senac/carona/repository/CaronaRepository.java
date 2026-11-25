package com.senac.carona.repository;

import com.senac.carona.model.Carona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaronaRepository extends JpaRepository<Carona, Long> {
}