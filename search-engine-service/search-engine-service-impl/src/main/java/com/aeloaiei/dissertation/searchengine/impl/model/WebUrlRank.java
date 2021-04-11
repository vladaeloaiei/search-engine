package com.aeloaiei.dissertation.searchengine.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web-url-ranks")
public class WebUrlRank {
    @MongoId
    private String location;
    private Float rank;
}
