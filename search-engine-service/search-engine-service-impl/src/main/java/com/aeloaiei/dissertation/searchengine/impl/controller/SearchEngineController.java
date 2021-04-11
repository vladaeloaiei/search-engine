package com.aeloaiei.dissertation.searchengine.impl.controller;

import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.searchengine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.searchengine.impl.service.SearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@RequestMapping("/search")
@RestController
public class SearchEngineController {
    @Autowired
    private SearchEngineService searchEngineService;

    @GetMapping
    public ResponseEntity<SearchResultDto> search(@RequestParam String query) {
        return ResponseEntity.ok(searchEngineService.search(query));
    }

    // Workaround to support large input query
    @PutMapping("/scoring")
    public ResponseEntity<ScoringSearchResultDto> scoringSearch(@RequestBody String query) {
        return ResponseEntity.ok(searchEngineService.scoringSearch(query));
    }

    @GetMapping("/scoring/pageranking")
    public ResponseEntity<ScoringSearchResultDto> scoringPageRankingSearch(@RequestParam String query) {
        return ResponseEntity.ok(searchEngineService.scoringPageRankingSearch(query));
    }
}
