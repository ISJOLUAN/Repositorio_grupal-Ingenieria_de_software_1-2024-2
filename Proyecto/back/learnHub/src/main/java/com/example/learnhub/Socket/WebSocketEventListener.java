package com.example.learnhub.Socket;

import com.example.learnhub.chat.ChatMessage;
import com.example.learnhub.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations operations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User " + username + " disconnected");
            var chatMessage = new ChatMessage().builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            operations.convertAndSend("/topic/public", chatMessage);
        }
    }

}

