package com.aeloaiei.dissertation.categoryhandler.impl.repository;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebIntro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebIntroRepository extends MongoRepository<WebIntro, String> {
}
