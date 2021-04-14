package com.aeloaiei.dissertation.subject.partitioner.repository;

import com.aeloaiei.dissertation.subject.partitioner.model.WebDocumentSubject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebDocumentSubjectRepository extends MongoRepository<WebDocumentSubject, String> {
    Optional<WebDocumentSubject> findByLocation(String words);
}
