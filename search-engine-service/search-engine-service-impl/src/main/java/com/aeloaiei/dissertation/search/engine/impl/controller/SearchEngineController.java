package com.aeloaiei.dissertation.search.engine.impl.controller;

import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SliceSearchResultDto;
import com.aeloaiei.dissertation.search.engine.impl.service.SearchEngineService;
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

    @GetMapping("/slice")
    public ResponseEntity<SliceSearchResultDto> sliceSearch(@RequestParam String query, @RequestParam int slice) {
        return ResponseEntity.ok(searchEngineService.sliceSearch(query, slice));
    }

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
