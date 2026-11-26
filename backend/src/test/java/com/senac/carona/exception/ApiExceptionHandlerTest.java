package com.senac.carona.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    @Test
    void devolve403ParaAcessoNegado() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<?> response = handler.handleAccessDenied(
                new AcessoNegadoException("Acesso restrito."));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
