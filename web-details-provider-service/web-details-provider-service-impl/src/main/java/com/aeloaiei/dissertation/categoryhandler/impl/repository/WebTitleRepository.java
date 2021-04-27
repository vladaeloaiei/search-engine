package com.aeloaiei.dissertation.categoryhandler.impl.repository;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebTitle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WebTitleRepository extends MongoRepository<WebTitle, String> {
}
