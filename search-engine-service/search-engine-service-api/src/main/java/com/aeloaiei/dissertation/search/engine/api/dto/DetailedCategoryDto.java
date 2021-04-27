package com.aeloaiei.dissertation.search.engine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailedCategoryDto {
    private String name;
    private Set<Page> pages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page {
        private String url;
        private String title;
        private String intro;
    }
}
