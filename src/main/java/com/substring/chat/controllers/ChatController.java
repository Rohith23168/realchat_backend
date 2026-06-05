package com.substring.chat.controllers;

import com.substring.chat.entities.Message;
import com.substring.chat.entities.Room;
import com.substring.chat.payload.MessageRequest;
import com.substring.chat.payload.MessageResponse;
import com.substring.chat.repositories.MessageRepository;
import com.substring.chat.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(
            RoomRepository roomRepository,
            MessageRepository messageRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // TYPING
    @MessageMapping("/typing")
    public void typing(@Payload MessageRequest request) {

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId() + "/typing",
                request.getSender()
        );
    }


    // ONLINE USERS
    @MessageMapping("/online")
    public void online(@Payload MessageRequest req) {

        messagingTemplate.convertAndSend(
                "/topic/room/" + req.getRoomId() + "/users",
                req.getSender()
        );
    }


    // REAL-TIME DELETE
    @MessageMapping("/delete")
    public void deleteMessageWS(@Payload Long messageId) {

        messagingTemplate.convertAndSend(
                "/topic/message/delete",
                messageId
        );
    }


    // SEND MESSAGE
    @Transactional
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageRequest request) {

        Room room = roomRepository.findByRoomId(request.getRoomId());

        if (room == null) return;

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setImageUrl(request.getImageUrl());
        message.setTimeStamp(LocalDateTime.now());
        message.setRoom(room);

        // STATUS
        message.setDelivered(true);
        message.setSeen(false);

        messageRepository.save(message);

        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSender(message.getSender());
        response.setContent(message.getContent());
        response.setImageUrl(message.getImageUrl());
        message.setAudioUrl(request.getAudioUrl());
        response.setTimeStamp(message.getTimeStamp());

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId(),
                response
        );
    }
}