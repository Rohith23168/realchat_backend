
package com.substring.chat.service;

import com.substring.chat.entities.Message;
import com.substring.chat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // THIS IS WHERE CODE GOES
    public Message saveMessage(Message message) {

        message.setDelivered(true);  //  message reached server
        message.setSeen(false);      //  user has NOT seen yet

        return messageRepository.save(message);
    }
}