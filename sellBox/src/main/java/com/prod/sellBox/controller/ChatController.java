package com.prod.sellBox.controller;


import com.prod.sellBox.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate so;

    @MessageMapping("chatBox/{roomId}")
    public void sendChat(ChatMessage chatMessage, @DestinationVariable String roomId) {
        log.info("chatMessage : {}", chatMessage.toString());
        String destination = "/chat/room/" + roomId;
        so.convertAndSend(destination, chatMessage);
    }

}
