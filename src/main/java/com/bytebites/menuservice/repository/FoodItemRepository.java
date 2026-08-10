package com.bytebites.menuservice.repository;

import com.bytebites.menuservice.model.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends MongoRepository<FoodItem, String> {
}
