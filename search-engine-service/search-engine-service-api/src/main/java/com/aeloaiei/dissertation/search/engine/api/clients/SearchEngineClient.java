package com.aeloaiei.dissertation.search.engine.api.clients;

import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SearchEngineClient", url = "${feign.client.search.engine.service.url}/search")
public interface SearchEngineClient {
    @GetMapping
    public SearchResultDto search(@RequestParam String query);

    @PutMapping("/scoring")
    public ScoringSearchResultDto scoringSearch(@RequestBody String query);

    @GetMapping("/scoring/pageranking")
    public ScoringSearchResultDto scoringPageRankingSearch(@RequestParam String query);
}
