package com.substring.chat.payload;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageResponse {

    private Long id;
    private String sender;
    private String content;
    private String imageUrl;
    private String audioUrl;
    private LocalDateTime timeStamp;
}