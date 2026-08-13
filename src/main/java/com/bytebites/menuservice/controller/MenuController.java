package com.bytebites.menuservice.controller;

import com.bytebites.menuservice.model.FoodItem;
import com.bytebites.menuservice.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private FoodItemRepository foodItemRepository;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/eca-uploads/";

    @GetMapping("/items")
    public ResponseEntity<List<FoodItem>> getAllItems() {
        try {
            List<FoodItem> items = foodItemRepository.findAll();
            return ResponseEntity.ok(items != null ? items : new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<FoodItem> getItemById(@PathVariable String id) {
        return foodItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public ResponseEntity<FoodItem> createItem(@RequestBody FoodItem item) {
        if (item.getId() == null || item.getId().isEmpty()) {
            item.setId(UUID.randomUUID().toString());
        }
        FoodItem saved = foodItemRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<FoodItem> updateItem(@PathVariable String id, @RequestBody FoodItem itemDetails) {
        try {
            FoodItem targetItem = foodItemRepository.findById(id).orElse(new FoodItem());

            targetItem.setId(id);
            targetItem.setName(itemDetails.getName());
            targetItem.setCategory(itemDetails.getCategory());
            targetItem.setPrice(itemDetails.getPrice());
            targetItem.setStock(itemDetails.getStock());

            if (itemDetails.getImageUrl() != null && !itemDetails.getImageUrl().isEmpty()) {
                targetItem.setImageUrl(itemDetails.getImageUrl());
            }

            FoodItem updated = foodItemRepository.save(targetItem);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        try {
            if (foodItemRepository.existsById(id)) {
                foodItemRepository.deleteById(id);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/items/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, filename);
            Files.write(path, file.getBytes());

            String fileUrl = "http://localhost:8080/api/menu/images/" + filename;
            return ResponseEntity.ok().body(new UploadResponse(fileUrl, filename));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(UPLOAD_DIR, filename);
            if (Files.exists(path)) {
                byte[] imageBytes = Files.readAllBytes(path);
                return ResponseEntity.ok()
                        .header("Content-Type", Files.probeContentType(path))
                        .body(imageBytes);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    static class UploadResponse {
        private String url;
        private String filename;

        public UploadResponse(String url, String filename) {
            this.url = url;
            this.filename = filename;
        }

        public String getUrl() { return url; }
        public String getFilename() { return filename; }
    }
}