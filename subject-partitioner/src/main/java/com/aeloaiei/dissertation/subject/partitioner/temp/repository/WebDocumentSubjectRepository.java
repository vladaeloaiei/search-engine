package com.aeloaiei.dissertation.subject.partitioner.temp.repository;

import com.aeloaiei.dissertation.subject.partitioner.temp.model.WebDocumentSubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebDocumentSubjectRepository extends MongoRepository<WebDocumentSubject, String> {
    Optional<WebDocumentSubject> findByLocation(String words);
}
