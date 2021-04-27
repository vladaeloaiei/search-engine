package com.aeloaiei.dissertation.search.engine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
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

        public static Comparator<Entry> comparingByScore() {
            return Comparator.comparing(Entry::getScore);
        }
    }
}
