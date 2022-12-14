package com.prod.sellBox.domain;

import lombok.Data;

@Data
public class ChatMessage {

    String roomId;
    String userId;
    String message;

}
