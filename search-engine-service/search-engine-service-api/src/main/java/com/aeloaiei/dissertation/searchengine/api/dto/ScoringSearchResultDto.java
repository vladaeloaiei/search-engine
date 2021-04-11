package com.aeloaiei.dissertation.searchengine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringSearchResultDto {
    private List<Entry> scores;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        private String url;
        private Float score;
    }
}
