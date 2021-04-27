package com.aeloaiei.dissertation.categoryhandler.impl.repository;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebUrlRank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WebUrlRankRepository extends MongoRepository<WebUrlRank, String> {
}
