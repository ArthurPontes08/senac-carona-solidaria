package com.senac.carona.dto;

import java.util.List;

public class ConversaResponse {

    private final Long requestId;
    private final String partner;
    private final String ride;
    private final List<ChatMessageResponse> messages;

    public ConversaResponse(Long requestId, String partner, String ride,
                            List<ChatMessageResponse> messages) {
        this.requestId = requestId;
        this.partner = partner;
        this.ride = ride;
        this.messages = List.copyOf(messages);
    }

    public Long getRequestId() { return requestId; }
    public String getPartner() { return partner; }
    public String getRide() { return ride; }
    public List<ChatMessageResponse> getMessages() { return messages; }
}
