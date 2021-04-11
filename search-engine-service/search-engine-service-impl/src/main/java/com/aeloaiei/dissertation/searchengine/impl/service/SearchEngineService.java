package com.aeloaiei.dissertation.searchengine.impl.service;

import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.searchengine.api.dto.SearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class SearchEngineService {
    @Autowired
    private ScoringWebSearchService scoringWebSearchService;

    public SearchResultDto search(String query) {
        //TODO add ranking
        return new SearchResultDto(scoringWebSearchService.search(query).entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(toList()));
    }

    public ScoringSearchResultDto scoringSearch(String query) {
        return new ScoringSearchResultDto(scoringWebSearchService.search(query)
                .entrySet()
                .stream()
                .map(entry -> new ScoringSearchResultDto.Entry(entry.getKey(), entry.getValue()))
                .collect(toList()));
    }

    public ScoringSearchResultDto scoringPageRankingSearch(String query) {
        return new ScoringSearchResultDto(scoringWebSearchService.search(query)
                .entrySet()
                .stream()
                .map(entry -> new ScoringSearchResultDto.Entry(entry.getKey(), entry.getValue()))
                .collect(toList()));
    }
}
