package com.aeloaiei.dissertation.categoryhandler.impl.repository;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebCategoryRepository extends MongoRepository<WebCategory, String> {
    Optional<WebCategory> findByCategory(String category);
}
