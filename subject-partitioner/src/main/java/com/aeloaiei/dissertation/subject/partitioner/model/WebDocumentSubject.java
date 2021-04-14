package com.aeloaiei.dissertation.subject.partitioner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web-document-subjects")
public class WebDocumentSubject implements Serializable {
    @MongoId
    private String location;
    private Set<Subject> subjects;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subject implements Serializable {
        private String subject;
        private Integer count;
    }
}
