package com.substring.chat.controllers;

import com.substring.chat.entities.Message;
import com.substring.chat.entities.Room;
import com.substring.chat.repositories.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://realchat-frontend-kohl.vercel.app"
})
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // =========================
    // CREATE ROOM (FIXED)
    // =========================
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Room room) {

        try {
            // clean roomId
            if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
                room.setRoomId(UUID.randomUUID().toString());
            }

            // prevent duplicate room
            Room existing = roomRepository.findByRoomId(room.getRoomId());
            if (existing != null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Room already exists");
            }

            // prevent null messages crash
            if (room.getMessages() == null) {
                room.setMessages(new ArrayList<>());
            }

            Room savedRoom = roomRepository.save(room);

            return ResponseEntity.ok(savedRoom);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create room: " + e.getMessage());
        }
    }


    // GET ALL ROOMS
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }


    // GET SINGLE ROOM
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Room not found");
        }

        return ResponseEntity.ok(room);
    }

    // GET ROOM MESSAGES
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Room not found");
        }

        List<Message> messages = room.getMessages();

        if (messages == null) {
            messages = new ArrayList<>();
        }

        return ResponseEntity.ok(messages);
    }


    // DELETE ROOM
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Room not found");
        }

        roomRepository.delete(room);

        return ResponseEntity.ok("Room deleted successfully");
    }
}