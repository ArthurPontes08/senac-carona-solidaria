package com.senac.carona.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApiExceptionHandlerTest {

    @Test
    void devolve403ParaAcessoNegado() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<?> response = handler.handleAccessDenied(
                new AcessoNegadoException("Acesso restrito."));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void devolve401ParaCredenciaisInvalidas() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<?> response = handler.handleInvalidCredentials(
                new CredenciaisInvalidasException());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void devolve409SemDetalhesInternosParaConflitoDeDados() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<?> response = handler.handleDataConflict(
                new DataIntegrityViolationException("secret SQL detail"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse(response.getBody().toString().contains("secret SQL detail"));
    }

    @Test
    void devolve503ParaFalhaDeAcessoAoBanco() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<?> response = handler.handleDatabaseUnavailable(
                new DataAccessResourceFailureException("database unavailable"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    void devolve400ParaParametroComTipoInvalido() {
        ApiExceptionHandler handler = new ApiExceptionHandler();
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "invalido", Integer.class, "vagas", null, new NumberFormatException());

        ResponseEntity<?> response = handler.handleInvalidParameter(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
