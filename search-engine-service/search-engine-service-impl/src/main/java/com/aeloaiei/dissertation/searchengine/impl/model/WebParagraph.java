package com.aeloaiei.dissertation.searchengine.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import static java.lang.Math.abs;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web-paragraphs")
public class WebParagraph {
    @MongoId
    private String id;
    private String location;
    private String content;
}
