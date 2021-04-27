package com.aeloaiei.dissertation.categoryhandler.impl.repository;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WebWordRepository extends MongoRepository<WebWord, String> {
}
