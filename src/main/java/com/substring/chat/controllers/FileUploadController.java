package com.substring.chat.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            File folder = new File(UPLOAD_DIR);
            if (!folder.exists()) folder.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + fileName);

            Files.copy(file.getInputStream(), path);

            String imageUrl = "http://localhost:8080/uploads/" + fileName;

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}