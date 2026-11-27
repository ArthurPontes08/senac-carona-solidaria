package com.senac.carona.repository;

import com.senac.carona.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmailSenacIgnoreCase(String emailSenac);

    Optional<Usuario> findByEmailSenacIgnoreCase(String emailSenac);
}