package com.aeloaiei.dissertation.web.details.provider.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebWordDto {
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
