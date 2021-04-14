package com.aeloaiei.dissertation.searchengine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private List<String> words;
    private List<Entry> pages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        private String url;
        private String title;
        private Set<String> paragraphs;

        public Entry(String url) {
            this.url = url;
            this.title = "";
            this.paragraphs = emptySet();
        }
    }
}
