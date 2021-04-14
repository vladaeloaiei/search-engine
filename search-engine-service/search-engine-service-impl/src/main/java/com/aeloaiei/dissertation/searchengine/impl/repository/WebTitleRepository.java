package com.aeloaiei.dissertation.searchengine.impl.repository;

import com.aeloaiei.dissertation.searchengine.impl.model.WebTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebTitleRepository extends MongoRepository<WebTitle, String> {
    Optional<WebTitle> findByLocation(String location);
}
