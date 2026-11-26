package com.senac.carona.dto;

import com.senac.carona.model.Usuario;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioApiDtoTest {

    @Test
    void convertePayloadDoFrontendParaUsuario() throws Exception {
        CadastroUsuarioRequest request = new ObjectMapper().readValue("""
                {
                  "name": "Joana",
                  "email": "joana@senacrs.edu.br",
                  "role": "driver",
                  "region": "Centro"
                }
                """, CadastroUsuarioRequest.class);

        Usuario usuario = request.toUsuario();

        assertEquals("Joana", usuario.getNome());
        assertEquals("joana@senacrs.edu.br", usuario.getEmailSenac());
        assertEquals("OFERECE", usuario.getPerfil());
        assertEquals("Centro", usuario.getBairroOrigem());
        assertEquals("ALUNO", usuario.getTipoVinculo());
        assertNull(usuario.getTelefone());
    }

    @Test
    void aceitaNomesPortuguesesComoAliasesLegados() throws Exception {
        CadastroUsuarioRequest request = new ObjectMapper().readValue("""
                {
                  "nome": "Joana",
                  "emailSenac": "joana@senacrs.edu.br",
                  "perfil": "BUSCA",
                  "bairroOrigem": "Centro"
                }
                """, CadastroUsuarioRequest.class);

        Usuario usuario = request.toUsuario();

        assertEquals("Joana", usuario.getNome());
        assertEquals("BUSCA", usuario.getPerfil());
        assertEquals("Centro", usuario.getBairroOrigem());
    }

    @Test
    void respostaUsaCamposDoContratoDoFrontendSemExporTelefone() throws Exception {
        Usuario usuario = new Usuario(7L, "Joana", "joana@senacrs.edu.br",
                "ALUNO", "BUSCA", "Centro", "(51) 90000-0000");

        UsuarioResponse response = UsuarioResponse.from(usuario);
        String json = new ObjectMapper().writeValueAsString(response);

        assertEquals(7L, response.getId());
        assertEquals("Joana", response.getName());
        assertEquals("joana@senacrs.edu.br", response.getEmail());
        assertEquals("rider", response.getRole());
        assertEquals("Centro", response.getRegion());
        assertTrue(json.contains("\"name\":\"Joana\""));
        assertFalse(json.contains("telefone"));
        assertFalse(json.contains("phone"));
    }
}
