package com.substring.chat.payload;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class MessageResponse {

    private Long id;
    private String sender;
    private String content;
    private String imageUrl;
    private String audioUrl;
    private OffsetDateTime timeStamp;
}