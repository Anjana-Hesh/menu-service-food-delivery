package com.bytebites.menuservice.controller;

import com.bytebites.menuservice.model.FoodItem;
import com.bytebites.menuservice.repository.FoodItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private FoodItemRepository foodItemRepository;

    private static final String UPLOAD_DIR = "C:/Users/LENOVO/.ijse/eca/storage";

    @PostConstruct
    public void init() {
        // Pre-populate menu catalog if database is empty to match the frontend expectations
        if (foodItemRepository.count() == 0) {
            foodItemRepository.save(new FoodItem("1", "Pancake Stack with Berries", "Desserts", 1490.00, 12, "https://images.unsplash.com/photo-1528207776546-365bb710ee93?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("2", "Classic Caesar Salad", "Burgers", 1250.00, 15, "https://images.unsplash.com/photo-1546793665-c74683f339c1?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("3", "Margherita Pizza", "Pizzas", 2850.00, 8, "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("4", "Grilled Beef Steak", "Burgers", 3450.00, 0, "https://images.unsplash.com/photo-1558030006-450675393462?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("5", "Creamy Carbonara Pasta", "Pizzas", 1850.00, 10, "https://images.unsplash.com/photo-1612874742237-6526221588e3?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("6", "Club Sandwich Deluxe", "Burgers", 1650.00, 20, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("7", "Herb Grilled Chicken", "Burgers", 2200.00, 5, "https://images.unsplash.com/photo-1598515214211-89d3c73ae83b?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("8", "Mediterranean Quinoa Bowl", "Drinks", 1350.00, 14, "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("9", "Chocolate Lava Cake", "Desserts", 950.00, 0, "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=500&q=80"));
            foodItemRepository.save(new FoodItem("10", "Iced Cappuccino", "Drinks", 850.00, 25, "https://images.unsplash.com/photo-1517701604599-bb29b565090c?auto=format&fit=crop&w=500&q=80"));
            System.out.println("Prepopulated 10 default dishes in MongoDB");
        }
    }

    @GetMapping("/items")
    public List<FoodItem> getAllItems() {
        return foodItemRepository.findAll();
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<FoodItem> getItemById(@PathVariable String id) {
        return foodItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/items")
    public FoodItem createItem(@RequestBody FoodItem item) {
        if (item.getId() == null || item.getId().isEmpty()) {
            item.setId(UUID.randomUUID().toString());
        }
        return foodItemRepository.save(item);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<FoodItem> updateItem(@PathVariable String id, @RequestBody FoodItem itemDetails) {
        return foodItemRepository.findById(id)
                .map(item -> {
                    item.setName(itemDetails.getName());
                    item.setCategory(itemDetails.getCategory());
                    item.setPrice(itemDetails.getPrice());
                    item.setStock(itemDetails.getStock());
                    if (itemDetails.getImageUrl() != null) {
                        item.setImageUrl(itemDetails.getImageUrl());
                    }
                    return ResponseEntity.ok(foodItemRepository.save(item));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        return foodItemRepository.findById(id)
                .map(item -> {
                    foodItemRepository.delete(item);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Google Cloud Storage Integration Endpoint Simulation
    // In production, we upload to google bucket, locally we write to local storage path
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

            // Mock public access strategy
            // Return public path or base64 or static serve URI
            // For ECA requirements we return public static URI string that frontend can render
            String fileUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=500&q=80"; // Fallback beautiful mock URL
            return ResponseEntity.ok().body(new UploadResponse(fileUrl, filename));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
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
