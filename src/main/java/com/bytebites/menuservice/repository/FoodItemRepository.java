package com.bytebites.menuservice.repository;

import com.bytebites.menuservice.model.FoodItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends CrudRepository<FoodItem, String> {
    @Override
    List<FoodItem> findAll();
}
