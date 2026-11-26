package com.senac.carona.service;

import com.senac.carona.dto.ChatMessageResponse;
import com.senac.carona.dto.ConversaResponse;
import com.senac.carona.model.Carona;
import com.senac.carona.model.MensagemChat;
import com.senac.carona.model.SolicitacaoCarona;
import com.senac.carona.model.Usuario;
import com.senac.carona.repository.CaronaRepository;
import com.senac.carona.repository.MensagemChatRepository;
import com.senac.carona.repository.SolicitacaoCaronaRepository;
import com.senac.carona.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    private final UsuarioRepository usuarioRepository;
    private final CaronaRepository caronaRepository;
    private final SolicitacaoCaronaRepository solicitacaoRepository;
    private final MensagemChatRepository mensagemRepository;

    public ChatService(UsuarioRepository usuarioRepository,
                       CaronaRepository caronaRepository,
                       SolicitacaoCaronaRepository solicitacaoRepository,
                       MensagemChatRepository mensagemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.mensagemRepository = mensagemRepository;
    }

    @Transactional(readOnly = true)
    public ConversaResponse buscarConversa(Long solicitacaoId, Long userId) {
        ChatContext context = validarParticipante(solicitacaoId, userId);
        List<ChatMessageResponse> mensagens = mensagemRepository
                .findBySolicitacaoIdOrderByCriadoEmAscIdAsc(solicitacaoId)
                .stream()
                .map(message -> {
                    if (message.getRemetenteId().equals(context.usuario().getId())) {
                        return new ChatMessageResponse(message, context.usuario().getNome(), true);
                    }
                    if (message.getRemetenteId().equals(context.parceiro().getId())) {
                        return new ChatMessageResponse(message, context.parceiro().getNome(), false);
                    }
                    throw new IllegalStateException(
                            "A conversa contém uma mensagem de um usuário que não participa da carona.");
                })
                .toList();

        return new ConversaResponse(solicitacaoId, context.parceiro().getNome(),
                resumoCarona(context.carona()), mensagens);
    }

    @Transactional
    public ChatMessageResponse enviarMensagem(Long solicitacaoId, Long userId, String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia.");
        }
        String mensagem = texto.trim();
        if (mensagem.length() > 500) {
            throw new IllegalArgumentException("A mensagem deve ter no máximo 500 caracteres.");
        }

        ChatContext context = validarParticipante(solicitacaoId, userId);
        MensagemChat salva = mensagemRepository.save(new MensagemChat(
                solicitacaoId, userId, mensagem, Instant.now()));
        return new ChatMessageResponse(salva, context.usuario().getNome(), true);
    }

    private ChatContext validarParticipante(Long solicitacaoId, Long userId) {
        if (solicitacaoId == null || solicitacaoId <= 0) {
            throw new IllegalArgumentException("requestId é obrigatório.");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId é obrigatório.");
        }

        SolicitacaoCarona solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
        if (!"CONFIRMADA".equalsIgnoreCase(solicitacao.getStatus())) {
            throw new IllegalStateException("A conversa só está disponível para solicitações confirmadas.");
        }
        Carona carona = caronaRepository.findById(solicitacao.getCaronaId())
                .orElseThrow(() -> new IllegalStateException("Carona vinculada não encontrada."));
        if (!userId.equals(carona.getMotoristaId())
                && !userId.equals(solicitacao.getPassageiroId())) {
            throw new IllegalArgumentException("Apenas participantes da carona podem acessar a conversa.");
        }

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        Long parceiroId = userId.equals(carona.getMotoristaId())
                ? solicitacao.getPassageiroId() : carona.getMotoristaId();
        Usuario parceiro = usuarioRepository.findById(parceiroId)
                .orElseThrow(() -> new IllegalStateException("Participante da carona não encontrado."));
        return new ChatContext(carona, usuario, parceiro);
    }

    private String resumoCarona(Carona carona) {
        return carona.getOrigem() + " → " + carona.getDestino()
                + " · " + carona.getData() + " às " + carona.getHorario();
    }

    private record ChatContext(Carona carona, Usuario usuario, Usuario parceiro) {
    }
}
