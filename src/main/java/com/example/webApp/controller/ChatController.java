package com.example.webApp.controller;

import com.example.webApp.models.Message;
import com.example.webApp.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import repositories.ConversationRepository;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    ConversationService conversationService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        try {
            System.out.println(message.toString());
//            message.setTimestamp(String.valueOf(LocalDateTime.now()));
            conversationService.addMessageToDocument(message);
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return message;
    }
}