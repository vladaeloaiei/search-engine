package com.aeloaiei.dissertation.searchengine.impl.repository;

import com.aeloaiei.dissertation.searchengine.impl.model.WebWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebWordRepository extends MongoRepository<WebWord, String> {
    Optional<WebWord> findByWord(String words);
}
