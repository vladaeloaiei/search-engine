package com.aeloaiei.dissertation.web.details.provider.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebDocumentSubjectDto {
    private String location;
    private Set<Subject> subjects;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Subject {
        private String subject;
        private Integer count;
    }
}
