package com.aeloaiei.dissertation.categoryhandler.impl.model;

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
@Document(collection = "web-categories")
public class WebCategory implements Serializable {
    @MongoId
    private String category;
    private Set<String> urls;
}
