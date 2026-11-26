package com.senac.carona.controller;

import com.senac.carona.dto.ChatMessageResponse;
import com.senac.carona.dto.ConversaResponse;
import com.senac.carona.dto.EnviarMensagemRequest;
import com.senac.carona.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes/{id}/conversa")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<ConversaResponse> buscarConversa(
            @PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(chatService.buscarConversa(id, userId));
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> enviarMensagem(
            @PathVariable Long id, @RequestBody EnviarMensagemRequest request) {
        ChatMessageResponse enviada = chatService.enviarMensagem(
                id, request.getUserId(), request.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(enviada);
    }

    @DeleteMapping
    public ResponseEntity<Void> apagarConversa(
            @PathVariable Long id, @RequestParam Long userId) {
        chatService.apagarConversa(id, userId);
        return ResponseEntity.noContent().build();
    }
}
