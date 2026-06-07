package com.substring.chat.controllers;

import com.substring.chat.entities.Message;
import com.substring.chat.entities.Room;
import com.substring.chat.payload.MessageRequest;
import com.substring.chat.payload.MessageResponse;
import com.substring.chat.repositories.MessageRepository;
import com.substring.chat.repositories.RoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/messages")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://chat-frontend-nine-puce.vercel.app"
})
public class MessageController {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(
            MessageRepository messageRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/seen")
    public void markSeen(@Payload Long messageId) {

        Message msg = messageRepository.findById(messageId).orElse(null);

        if (msg != null) {
            msg.setSeen(true);
            messageRepository.save(msg);

            messagingTemplate.convertAndSend(
                    "/topic/message/seen",
                    messageId
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {

        if (!messageRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        messageRepository.deleteById(id);

        return ResponseEntity.ok("Message Deleted");
    }
}