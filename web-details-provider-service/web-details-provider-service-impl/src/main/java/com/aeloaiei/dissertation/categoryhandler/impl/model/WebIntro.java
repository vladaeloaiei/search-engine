package com.aeloaiei.dissertation.categoryhandler.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "web-intros")
public class WebIntro {
    @MongoId
    private String location;
    private String content;
}
