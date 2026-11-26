package com.senac.carona.dto;

import com.senac.carona.model.MensagemChat;

public class ChatMessageResponse {

    private final Long id;
    private final String sender;
    private final String text;
    private final String createdAt;
    private final boolean mine;

    public ChatMessageResponse(MensagemChat message, String sender, boolean mine) {
        this.id = message.getId();
        this.sender = sender;
        this.text = message.getTexto();
        this.createdAt = message.getCriadoEm().toString();
        this.mine = mine;
    }

    public Long getId() { return id; }
    public String getSender() { return sender; }
    public String getText() { return text; }
    public String getCreatedAt() { return createdAt; }
    public boolean isMine() { return mine; }
}
