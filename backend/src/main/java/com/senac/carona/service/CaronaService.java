package com.senac.carona.service;

import com.senac.carona.dto.MinhasCaronasResponse;
import com.senac.carona.dto.RecomendacaoCaronaResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CaronaService {
    public static final String STATUS_ABERTA = "ABERTA";
    public static final String STATUS_LOTADA = "LOTADA";
    public static final String STATUS_CANCELADA = "CANCELADA";
    public static final String SOLICITACAO_PENDENTE = "PENDENTE";
    public static final String SOLICITACAO_CONFIRMADA = "CONFIRMADA";
    public static final String SOLICITACAO_RECUSADA = "RECUSADA";
    public static final String SOLICITACAO_CANCELADA = "CANCELADA";

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Carona> caronas = new ArrayList<>();
    private final List<SolicitacaoCarona> solicitacoes = new ArrayList<>();

    private final AtomicLong usuarioSeq = new AtomicLong(1);
    private final AtomicLong caronaSeq = new AtomicLong(1);
    private final AtomicLong solicitacaoSeq = new AtomicLong(1);
    private static final DateTimeFormatter DATA_FORMATO = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter HORARIO_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    public CaronaService() {
        String hoje = LocalDate.now().format(DATA_FORMATO);
        Usuario arthur = cadastrarUsuario(new Usuario(null, "Arthur Pontes",
                "arthur.pontes@senacrs.com.br", "ALUNO", "OFERECE",
                "Mathias Velho", "(51) 99999-1111"));

        Usuario livia = cadastrarUsuario(new Usuario(null, "Lívia Silva",
                "livia.silva@senacrs.com.br", "ALUNO", "AMBOS",
                "Centro Canoas", "(51) 98888-2222"));

        cadastrarUsuario(new Usuario(null, "João Souza",
                "joao.souza@senacrs.com.br", "ALUNO", "BUSCA",
                "Niterói", "(51) 97777-3333"));

        publicarCarona(new Carona(null, arthur.getId(), null, null,
                "Mathias Velho - Canoas", "Senac Canoas",
                hoje, "23:59", "Passarela Estação Mathias Velho",
                3, "ABERTA", "Saída pontual rumo às aulas"));

        publicarCarona(new Carona(null, livia.getId(), null, null,
                "Centro - Canoas", "Senac Canoas",
                hoje, "23:59", "Em frente à Praça da Emancipação",
                2, "ABERTA", "Trajeto direto sem paradas extras"));
    }

    public synchronized Usuario cadastrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Os dados do usuário são obrigatórios.");
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (usuario.getNome().trim().length() > 120) {
            throw new IllegalArgumentException("Nome deve ter no máximo 120 caracteres.");
        }
        if (usuario.getBairroOrigem() == null || usuario.getBairroOrigem().isBlank()) {
            throw new IllegalArgumentException("Bairro/região de origem é obrigatório.");
        }
        if (usuario.getBairroOrigem().trim().length() > 120) {
            throw new IllegalArgumentException("Bairro/região deve ter no máximo 120 caracteres.");
        }
        String email = usuario.getEmailSenac() == null ? "" : usuario.getEmailSenac().trim().toLowerCase(Locale.ROOT);
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]*senac[A-Za-z0-9.-]*\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("É necessário usar um e-mail válido da comunidade Senac.");
        }
        if (usuarios.stream().anyMatch(u -> u.getEmailSenac().equalsIgnoreCase(email))) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }
        String vinculo = (usuario.getTipoVinculo() == null || usuario.getTipoVinculo().isBlank())
                ? "ALUNO" : usuario.getTipoVinculo().toUpperCase();
        if (!Set.of("ALUNO", "FUNCIONARIO").contains(vinculo)) {
            throw new IllegalArgumentException("Vínculo inválido. Use ALUNO ou FUNCIONARIO.");
        }
        String perfil = (usuario.getPerfil() == null || usuario.getPerfil().isBlank())
                ? "AMBOS" : usuario.getPerfil().toUpperCase();
        if (!Set.of("OFERECE", "BUSCA", "AMBOS").contains(perfil)) {
            throw new IllegalArgumentException("Perfil inválido. Use OFERECE, BUSCA ou AMBOS.");
        }
        if (usuario.getTelefone() == null || usuario.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório.");
        }
        if (!usuario.getTelefone().matches("^[0-9()+ -]{8,25}$")) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        usuario.setEmailSenac(email);
        usuario.setId(usuarioSeq.getAndIncrement());
        usuario.setTipoVinculo(vinculo);
        usuario.setPerfil(perfil);
        usuarios.add(usuario);
        return usuario;
    }

    public synchronized List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public synchronized Optional<Usuario> buscarUsuarioPorId(Long id) {
        if (id == null || id <= 0) return Optional.empty();
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public synchronized Carona publicarCarona(Carona novaCarona) {
        if (novaCarona == null) {
            throw new IllegalArgumentException("Os dados da carona são obrigatórios.");
        }
        if (novaCarona.getMotoristaId() == null) {
            throw new IllegalArgumentException("MotoristaId é obrigatório.");
        }
        Usuario motorista = buscarUsuarioPorId(novaCarona.getMotoristaId())
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado. Cadastre o usuário primeiro."));

        if (novaCarona.getOrigem() == null || novaCarona.getOrigem().isBlank() ||
            novaCarona.getDestino() == null || novaCarona.getDestino().isBlank()) {
            throw new IllegalArgumentException("Origem e destino são obrigatórios.");
        }
        if (novaCarona.getData() == null || novaCarona.getData().isBlank() ||
            novaCarona.getHorario() == null || novaCarona.getHorario().isBlank()) {
            throw new IllegalArgumentException("Data e horário são obrigatórios.");
        }
        try {
            LocalDate data = LocalDate.parse(novaCarona.getData().trim(), DATA_FORMATO);
            if (data.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Não é permitido publicar carona em data passada.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
        }
        try {
            LocalTime.parse(novaCarona.getHorario().trim(), HORARIO_FORMATO);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Horário inválido. Use o formato HH:mm.");
        }
        if (novaCarona.getVagasTotais() <= 0) {
            throw new IllegalArgumentException("A carona deve ter pelo menos 1 vaga.");
        }
        if (novaCarona.getVagasTotais() > 8) {
            throw new IllegalArgumentException("Uma carona pode ter no máximo 8 vagas.");
        }
        if (novaCarona.getOrigem().trim().length() > 150
                || novaCarona.getDestino().trim().length() > 150) {
            throw new IllegalArgumentException("Origem e destino devem ter no máximo 150 caracteres.");
        }
        if ("BUSCA".equalsIgnoreCase(motorista.getPerfil())) {
            throw new IllegalArgumentException("Este usuário está configurado apenas para buscar caronas.");
        }
        if (novaCarona.getStatus() != null
                && !"ABERTA".equalsIgnoreCase(novaCarona.getStatus())) {
            throw new IllegalArgumentException("Uma nova carona deve iniciar com o status ABERTA.");
        }

        novaCarona.setId(caronaSeq.getAndIncrement());
        novaCarona.setMotoristaNome(motorista.getNome());
        novaCarona.setVinculoSenac(motorista.getTipoVinculo() + " • Comunidade Senac");
        novaCarona.setVagasDisponiveis(novaCarona.getVagasTotais());
        novaCarona.setStatus("ABERTA");

        caronas.add(novaCarona);
        return novaCarona;
    }

    public synchronized List<Carona> buscarCaronas(String data, String regiao) {
        if (data != null && !data.isBlank()) {
            try {
                LocalDate.parse(data.trim(), DATA_FORMATO);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
            }
        }
        return caronas.stream()
                .filter(c -> !"CANCELADA".equalsIgnoreCase(c.getStatus()))
                .filter(c -> {
                    try {
                        LocalDate dia = LocalDate.parse(c.getData(), DATA_FORMATO);
                        if (dia.isBefore(LocalDate.now())) return false;
                        if (dia.isEqual(LocalDate.now())) {
                            LocalTime horario = LocalTime.parse(c.getHorario(), HORARIO_FORMATO);
                            return horario.isAfter(LocalTime.now());
                        }
                        return true;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .filter(c -> data == null || data.isBlank() || c.getData().equalsIgnoreCase(data.trim()))
                .filter(c -> {
                    if (regiao == null || regiao.isBlank()) return true;
                    String termo = regiao.toLowerCase().trim();
                    return c.getOrigem().toLowerCase().contains(termo)
                            || c.getDestino().toLowerCase().contains(termo)
                            || (c.getPontoEncontro() != null && c.getPontoEncontro().toLowerCase().contains(termo));
                })
                .collect(Collectors.toUnmodifiableList());
    }

    public synchronized Optional<Carona> buscarCaronaPorId(Long id) {
        if (id == null || id <= 0) return Optional.empty();
        return caronas.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public synchronized SolicitacaoCarona solicitarVaga(Long caronaId, Long passageiroId, String pontoEmbarque) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        if (passageiroId == null || passageiroId <= 0) {
            throw new IllegalArgumentException("PassageiroId é obrigatório.");
        }
        Carona carona = buscarCaronaPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));

        if (carona.getMotoristaId().equals(passageiroId)) {
            throw new IllegalArgumentException("O motorista não pode solicitar vaga na própria carona.");
        }

        try {
            LocalDate dia = LocalDate.parse(carona.getData(), DATA_FORMATO);
            if (dia.isBefore(LocalDate.now())) {
                throw new IllegalStateException("Não é possível solicitar vaga em uma carona passada.");
            }
            if (dia.isEqual(LocalDate.now())
                    && !LocalTime.parse(carona.getHorario(), HORARIO_FORMATO).isAfter(LocalTime.now())) {
                throw new IllegalStateException("Não é possível solicitar vaga após o horário da carona.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Esta carona possui data ou horário inválido.");
        }

        if (!"ABERTA".equalsIgnoreCase(carona.getStatus()) || carona.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Esta carona não possui vagas disponíveis no momento.");
        }

        Usuario passageiro = buscarUsuarioPorId(passageiroId)
                .orElseThrow(() -> new IllegalArgumentException("Passageiro não encontrado."));
        if ("OFERECE".equalsIgnoreCase(passageiro.getPerfil())) {
            throw new IllegalArgumentException("Este usuário está configurado apenas para oferecer caronas.");
        }
        boolean jaSolicitou = solicitacoes.stream()
                .anyMatch(s -> s.getCaronaId().equals(caronaId)
                        && s.getPassageiroId().equals(passageiroId)
                        && Set.of("PENDENTE", "CONFIRMADA").contains(s.getStatus()));
        if (jaSolicitou) {
            throw new IllegalStateException("Este usuário já possui uma solicitação ativa para esta carona.");
        }

        SolicitacaoCarona solicitacao = new SolicitacaoCarona(
                solicitacaoSeq.getAndIncrement(),
                carona.getId(),
                passageiro.getId(),
                passageiro.getNome(),
                (pontoEmbarque == null || pontoEmbarque.isBlank()) ? carona.getPontoEncontro() : pontoEmbarque,
                "PENDENTE",
                null
        );

        solicitacoes.add(solicitacao);
        return solicitacao;
    }

    public synchronized SolicitacaoCarona responderSolicitacao(Long solicitacaoId, String acao) {
        return responderSolicitacao(solicitacaoId, null, acao);
    }

    public synchronized SolicitacaoCarona responderSolicitacao(
            Long solicitacaoId, Long motoristaId, String acao) {
        if (solicitacaoId == null || solicitacaoId <= 0) {
            throw new IllegalArgumentException("SolicitacaoId é obrigatório.");
        }
        SolicitacaoCarona sol = solicitacoes.stream()
                .filter(s -> s.getId().equals(solicitacaoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        Carona carona = buscarCaronaPorId(sol.getCaronaId())
                .orElseThrow(() -> new IllegalArgumentException("Carona vinculada não encontrada."));
        if (motoristaId != null && !motoristaId.equals(carona.getMotoristaId())) {
            throw new IllegalArgumentException("Apenas o motorista desta carona pode responder à solicitação.");
        }

        String decisao = acao == null ? "" : acao.toUpperCase().trim();

        switch (decisao) {
            case "ACEITAR":
            case "CONFIRMAR":
                if (!"PENDENTE".equalsIgnoreCase(sol.getStatus())) {
                    throw new IllegalStateException("Apenas solicitações pendentes podem ser confirmadas.");
                }
                if (carona.getVagasDisponiveis() <= 0) {
                    throw new IllegalStateException("Não há vagas restantes para confirmar esta solicitação.");
                }
                sol.setStatus("CONFIRMADA");
                int novasVagas = carona.getVagasDisponiveis() - 1;
                carona.setVagasDisponiveis(novasVagas);
                if (novasVagas == 0) {
                    carona.setStatus("LOTADA");
                }
                buscarUsuarioPorId(carona.getMotoristaId())
                        .ifPresent(m -> sol.setTelefoneMotoristaLiberado(m.getTelefone()));
                break;

            case "RECUSAR":
                if (!"PENDENTE".equalsIgnoreCase(sol.getStatus())) {
                    throw new IllegalStateException("Apenas solicitações pendentes podem ser recusadas.");
                }
                sol.setStatus("RECUSADA");
                sol.setTelefoneMotoristaLiberado(null);
                break;

            case "CANCELAR":
                if (!Set.of("PENDENTE", "CONFIRMADA").contains(sol.getStatus().toUpperCase())) {
                    throw new IllegalStateException("Esta solicitação já está encerrada e não pode ser cancelada.");
                }
                if ("CONFIRMADA".equalsIgnoreCase(sol.getStatus())) {
                    int vagas = carona.getVagasDisponiveis();
                    if (vagas >= carona.getVagasTotais()) {
                        throw new IllegalStateException("Não é possível devolver uma vaga inexistente.");
                    }
                    vagas++;
                    carona.setVagasDisponiveis(vagas);
                    carona.setStatus("CANCELADA".equalsIgnoreCase(carona.getStatus()) ? "CANCELADA" : "ABERTA");
                }
                sol.setStatus("CANCELADA");
                sol.setTelefoneMotoristaLiberado(null);
                break;

            default:
                throw new IllegalArgumentException("Ação inválida. Use ACEITAR, RECUSAR ou CANCELAR.");
        }

        return sol;
    }

    public synchronized Carona cancelarCarona(Long caronaId, Long motoristaId) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        if (motoristaId == null || motoristaId <= 0) {
            throw new IllegalArgumentException("MotoristaId é obrigatório.");
        }
        Carona carona = buscarCaronaPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        if (!motoristaId.equals(carona.getMotoristaId())) {
            throw new IllegalArgumentException("Apenas o motorista desta carona pode cancelá-la.");
        }
        if (STATUS_CANCELADA.equalsIgnoreCase(carona.getStatus())) {
            throw new IllegalStateException("Esta carona já está cancelada.");
        }
        carona.setStatus(STATUS_CANCELADA);
        solicitacoes.stream()
                .filter(s -> s.getCaronaId().equals(caronaId))
                .filter(s -> SOLICITACAO_PENDENTE.equalsIgnoreCase(s.getStatus())
                        || SOLICITACAO_CONFIRMADA.equalsIgnoreCase(s.getStatus()))
                .forEach(s -> {
                    s.setStatus(SOLICITACAO_CANCELADA);
                    s.setTelefoneMotoristaLiberado(null);
                });
        return carona;
    }

    public synchronized MinhasCaronasResponse minhasCaronas(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("UsuarioId é obrigatório.");
        }
        buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        List<Carona> oferecidas = caronas.stream()
                .filter(c -> c.getMotoristaId() != null && c.getMotoristaId().equals(usuarioId))
                .collect(Collectors.toUnmodifiableList());

        List<SolicitacaoCarona> pedidas = solicitacoes.stream()
                .filter(s -> s.getPassageiroId() != null && s.getPassageiroId().equals(usuarioId))
                .collect(Collectors.toUnmodifiableList());

        return new MinhasCaronasResponse(oferecidas, pedidas);
    }

    public synchronized List<SolicitacaoCarona> listarSolicitacoesPorCarona(Long caronaId) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        buscarCaronaPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        return solicitacoes.stream()
                .filter(s -> s.getCaronaId().equals(caronaId))
                .collect(Collectors.toUnmodifiableList());
    }

    public synchronized List<RecomendacaoCaronaResponse> recomendarCaronas(String data, String regiao, String horarioDesejado) {
        if (data != null && !data.isBlank()) {
            try {
                LocalDate.parse(data.trim(), DATA_FORMATO);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
            }
        }
        List<Carona> candidatas = buscarCaronas(data, null);

        LocalTime alvo = null;
        if (horarioDesejado != null && !horarioDesejado.isBlank()) {
            try {
                alvo = LocalTime.parse(horarioDesejado.trim(), HORARIO_FORMATO);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Horário desejado inválido. Use o formato HH:mm.");
            }
        }

        LocalTime finalAlvo = alvo;
        return candidatas.stream()
                .map(c -> {
                    int score = 50;
                    boolean apertado = false;
                    List<String> motivos = new ArrayList<>();

                    if (regiao != null && !regiao.isBlank()) {
                        String r = regiao.toLowerCase().trim();
                        if (c.getOrigem().toLowerCase().contains(r) ||
                            (c.getPontoEncontro() != null && c.getPontoEncontro().toLowerCase().contains(r))) {
                            score += 30;
                            motivos.add("região compatível com " + c.getOrigem());
                        }
                    }

                    if (c.getVagasDisponiveis() > 0) {
                        score += 10;
                    }

                    if (finalAlvo != null) {
                        try {
                            LocalTime saida = LocalTime.parse(c.getHorario().trim(), HORARIO_FORMATO);
                            long minutosDiff = Math.abs(java.time.Duration.between(finalAlvo, saida).toMinutes());
                            if (minutosDiff <= 20) {
                                score += 10;
                                motivos.add("horário próximo (" + c.getHorario() + ")");
                            }
                            if (minutosDiff > 0 && minutosDiff <= 15) {
                                apertado = true;
                                motivos.add("atenção: encaixe de horário apertado (" + minutosDiff + " min de diferença)");
                            }
                        } catch (DateTimeParseException e) {
                            motivos.add("horário cadastrado inválido e ignorado");
                        }
                    }

                    if (motivos.isEmpty()) {
                        motivos.add("opção disponível para o dia " + c.getData() + " às " + c.getHorario());
                    }

                    String resumo = "Carona de " + c.getMotoristaNome() + " (" + c.getOrigem() + " → " + c.getDestino() +
                            ") — " + String.join("; ", motivos) + ".";

                    return new RecomendacaoCaronaResponse(c, Math.min(score, 100), apertado, resumo);
                })
                .sorted((a, b) -> Integer.compare(b.getScoreCompatibilidade(), a.getScoreCompatibilidade()))
                .collect(Collectors.toUnmodifiableList());
    }
}