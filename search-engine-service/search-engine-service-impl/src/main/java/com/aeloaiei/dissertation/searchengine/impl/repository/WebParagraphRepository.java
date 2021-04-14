package com.aeloaiei.dissertation.searchengine.impl.repository;

import com.aeloaiei.dissertation.searchengine.impl.model.WebParagraph;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebParagraphRepository extends MongoRepository<WebParagraph, String> {
}
