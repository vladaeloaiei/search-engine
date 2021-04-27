package com.aeloaiei.dissertation.categoryhandler.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web-words")
public class WebWord {
    @MongoId
    private String word;
    private Float inverseDocumentFrequencySmooth;
    private Set<Appearance> appearances;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Appearance {
        private String location;
        private Float logNormalizedTermFrequency;
        private Set<String> paragraphsIds;
    }
}
