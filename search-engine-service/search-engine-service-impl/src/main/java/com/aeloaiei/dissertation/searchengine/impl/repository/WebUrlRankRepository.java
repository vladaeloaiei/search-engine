package com.aeloaiei.dissertation.searchengine.impl.repository;

import com.aeloaiei.dissertation.searchengine.impl.model.WebUrlRank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebUrlRankRepository extends MongoRepository<WebUrlRank, String> {
}
