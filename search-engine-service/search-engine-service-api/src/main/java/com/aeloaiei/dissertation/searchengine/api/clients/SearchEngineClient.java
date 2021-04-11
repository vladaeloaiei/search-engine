package com.aeloaiei.dissertation.searchengine.api.clients;

import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.searchengine.api.dto.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "WebSearchClient", url = "http://localhost:10001/search")
public interface SearchEngineClient {
    @GetMapping
    public SearchResultDto search(@RequestParam String query);

    @PutMapping("/scoring")
    public ScoringSearchResultDto scoringSearch(@RequestBody String query);

    @GetMapping("/scoring/pageranking")
    public ScoringSearchResultDto scoringPageRankingSearch(@RequestParam String query);
}
