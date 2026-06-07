package com.substring.chat.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://realchat-frontend-kohl.vercel.app"
})
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {

            // 1. Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // 2. Ensure folder exists
            File folder = new File(UPLOAD_DIR);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 3. Create unique file name
            String fileName = System.currentTimeMillis()
                    + "_" + file.getOriginalFilename();

            // 4. FIXED PATH (IMPORTANT)
            Path path = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // 5. Build BASE URL safely (NO ENV dependency)
            String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            // 6. Final image URL
            String imageUrl = baseUrl + "/uploads/" + fileName;

            System.out.println("UPLOAD SUCCESS: " + imageUrl);

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Upload failed: " + e.getMessage());
        }
    }
}