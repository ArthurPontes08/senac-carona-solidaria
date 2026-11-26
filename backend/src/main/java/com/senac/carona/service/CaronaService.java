package com.senac.carona.service;

import com.senac.carona.dto.MinhasCaronasResponse;
import com.senac.carona.dto.RecomendacaoCaronaResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import com.senac.carona.repository.CaronaRepository;
import com.senac.carona.repository.SolicitacaoCaronaRepository;
import com.senac.carona.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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

    private final UsuarioRepository usuarioRepository;
    private final CaronaRepository caronaRepository;
    private final SolicitacaoCaronaRepository solicitacaoCaronaRepository;
    private static final DateTimeFormatter DATA_FORMATO = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter HORARIO_FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    public CaronaService(UsuarioRepository usuarioRepository,
                         CaronaRepository caronaRepository,
                         SolicitacaoCaronaRepository solicitacaoCaronaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
        this.solicitacaoCaronaRepository = solicitacaoCaronaRepository;
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
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
        if (usuarioRepository.existsByEmailSenacIgnoreCase(email)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }
        String vinculo = (usuario.getTipoVinculo() == null || usuario.getTipoVinculo().isBlank())
                ? "ALUNO" : usuario.getTipoVinculo().trim().toUpperCase(Locale.ROOT);
        if (!Set.of("ALUNO", "FUNCIONARIO").contains(vinculo)) {
            throw new IllegalArgumentException("Vínculo inválido. Use ALUNO ou FUNCIONARIO.");
        }
        String perfil = (usuario.getPerfil() == null || usuario.getPerfil().isBlank())
                ? "AMBOS" : usuario.getPerfil().trim().toUpperCase(Locale.ROOT);
        if (!Set.of("OFERECE", "BUSCA", "AMBOS").contains(perfil)) {
            throw new IllegalArgumentException("Perfil inválido. Use OFERECE, BUSCA ou AMBOS.");
        }
        String telefone = usuario.getTelefone();
        if (telefone != null && !telefone.isBlank()
                && !telefone.matches("^[0-9()+ -]{8,25}$")) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        usuario.setEmailSenac(email);
        usuario.setNome(usuario.getNome().trim());
        usuario.setBairroOrigem(usuario.getBairroOrigem().trim());
        usuario.setTelefone(telefone == null || telefone.isBlank() ? null : telefone.trim());
        usuario.setId(null);
        usuario.setTipoVinculo(vinculo);
        usuario.setPerfil(perfil);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        if (id == null || id <= 0) return Optional.empty();
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Carona publicarCarona(Carona novaCarona) {
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

        novaCarona.setId(null);
        novaCarona.setMotoristaNome(motorista.getNome());
        novaCarona.setVinculoSenac(motorista.getTipoVinculo() + " • Comunidade Senac");
        novaCarona.setVagasDisponiveis(novaCarona.getVagasTotais());
        novaCarona.setStatus("ABERTA");

        return caronaRepository.save(novaCarona);
    }

    @Transactional(readOnly = true)
    public List<Carona> buscarCaronas(String data, String regiao) {
        if (data != null && !data.isBlank()) {
            try {
                LocalDate.parse(data.trim(), DATA_FORMATO);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
            }
        }
        return caronaRepository.findAll().stream()
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

    @Transactional(readOnly = true)
    public List<Carona> buscarOfertas(
            String data, String regiao, String horario, Integer vagasMinimas) {
        LocalTime horarioMinimo = null;
        if (horario != null && !horario.isBlank()) {
            try {
                horarioMinimo = LocalTime.parse(horario.trim(), HORARIO_FORMATO);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Horário inválido. Use o formato HH:mm.");
            }
        }
        int minimo = vagasMinimas == null ? 1 : vagasMinimas;
        if (minimo < 1 || minimo > 8) {
            throw new IllegalArgumentException("O filtro de vagas deve estar entre 1 e 8.");
        }

        LocalTime finalHorarioMinimo = horarioMinimo;
        return buscarCaronas(data, regiao).stream()
                .filter(c -> STATUS_ABERTA.equalsIgnoreCase(c.getStatus()))
                .filter(c -> c.getVagasDisponiveis() >= minimo)
                .filter(c -> {
                    if (finalHorarioMinimo == null) return true;
                    try {
                        return !LocalTime.parse(c.getHorario(), HORARIO_FORMATO)
                                .isBefore(finalHorarioMinimo);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public Optional<Carona> buscarCaronaPorId(Long id) {
        if (id == null || id <= 0) return Optional.empty();
        return caronaRepository.findById(id);
    }

    @Transactional
    public SolicitacaoCarona solicitarVaga(Long caronaId, Long passageiroId, String pontoEmbarque) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        if (passageiroId == null || passageiroId <= 0) {
            throw new IllegalArgumentException("PassageiroId é obrigatório.");
        }
        Carona carona = caronaRepository.findByIdForUpdate(caronaId)
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
        boolean jaSolicitou = solicitacaoCaronaRepository
                .existsByCaronaIdAndPassageiroIdAndStatusIn(
                        caronaId, passageiroId, List.of("PENDENTE", "CONFIRMADA"));
        if (jaSolicitou) {
            throw new IllegalStateException("Este usuário já possui uma solicitação ativa para esta carona.");
        }

        String pontoFinal = (pontoEmbarque == null || pontoEmbarque.isBlank())
                ? carona.getPontoEncontro() : pontoEmbarque.trim();
        if (pontoFinal == null || pontoFinal.isBlank()) {
            throw new IllegalArgumentException("Ponto de embarque é obrigatório.");
        }

        SolicitacaoCarona solicitacao = new SolicitacaoCarona(
                null,
                carona.getId(),
                passageiro.getId(),
                passageiro.getNome(),
                pontoFinal,
                "PENDENTE",
                null
        );

        return solicitacaoCaronaRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoCarona responderSolicitacao(Long solicitacaoId, String acao) {
        return responderSolicitacao(solicitacaoId, null, acao);
    }

    @Transactional
    public SolicitacaoCarona responderSolicitacao(
            Long solicitacaoId, Long motoristaId, String acao) {
        if (solicitacaoId == null || solicitacaoId <= 0) {
            throw new IllegalArgumentException("SolicitacaoId é obrigatório.");
        }
        Long caronaId = solicitacaoCaronaRepository.findCaronaIdById(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        Carona carona = caronaRepository.findByIdForUpdate(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona vinculada não encontrada."));
        SolicitacaoCarona sol = solicitacaoCaronaRepository.findByIdForUpdate(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

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
                usuarioRepository.findById(carona.getMotoristaId())
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

        solicitacaoCaronaRepository.save(sol);
        caronaRepository.save(carona);
        return sol;
    }

    @Transactional
    public Carona cancelarCarona(Long caronaId, Long motoristaId) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        if (motoristaId == null || motoristaId <= 0) {
            throw new IllegalArgumentException("MotoristaId é obrigatório.");
        }
        Carona carona = caronaRepository.findByIdForUpdate(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        if (!motoristaId.equals(carona.getMotoristaId())) {
            throw new IllegalArgumentException("Apenas o motorista desta carona pode cancelá-la.");
        }
        if (STATUS_CANCELADA.equalsIgnoreCase(carona.getStatus())) {
            throw new IllegalStateException("Esta carona já está cancelada.");
        }
        carona.setStatus(STATUS_CANCELADA);
        List<SolicitacaoCarona> solicitacoes = solicitacaoCaronaRepository.findByCaronaId(caronaId);
        solicitacoes.stream()
                .filter(s -> s.getCaronaId().equals(caronaId))
                .filter(s -> SOLICITACAO_PENDENTE.equalsIgnoreCase(s.getStatus())
                        || SOLICITACAO_CONFIRMADA.equalsIgnoreCase(s.getStatus()))
                .forEach(s -> {
                    s.setStatus(SOLICITACAO_CANCELADA);
                    s.setTelefoneMotoristaLiberado(null);
                });
        solicitacaoCaronaRepository.saveAll(solicitacoes);
        return caronaRepository.save(carona);
    }

    @Transactional(readOnly = true)
    public MinhasCaronasResponse minhasCaronas(Long usuarioId) {
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("UsuarioId é obrigatório.");
        }
        buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        List<Carona> oferecidas = caronaRepository.findByMotoristaId(usuarioId);
        List<SolicitacaoCarona> pedidas = solicitacaoCaronaRepository.findByPassageiroId(usuarioId);

        return new MinhasCaronasResponse(oferecidas, pedidas);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCarona> listarSolicitacoesPorCarona(Long caronaId) {
        if (caronaId == null || caronaId <= 0) {
            throw new IllegalArgumentException("CaronaId é obrigatório.");
        }
        buscarCaronaPorId(caronaId)
                .orElseThrow(() -> new IllegalArgumentException("Carona não encontrada."));
        return solicitacaoCaronaRepository.findByCaronaId(caronaId);
    }

    @Transactional(readOnly = true)
    public List<RecomendacaoCaronaResponse> recomendarCaronas(
            String data, String regiao, String horarioDesejado) {
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