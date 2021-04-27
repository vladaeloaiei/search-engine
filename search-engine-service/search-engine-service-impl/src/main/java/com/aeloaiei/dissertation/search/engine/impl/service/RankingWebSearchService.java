package com.aeloaiei.dissertation.search.engine.impl.service;

import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebUrlRankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class RankingWebSearchService {
    @Autowired
    private WebUrlRankClient webUrlRankClient;

    public ScoringSearchResultDto rank(ScoringSearchResultDto scores) {
        List<String> urls = scores.getScores().stream()
                .map(ScoringSearchResultDto.Entry::getUrl)
                .collect(toList());

        List<ScoringSearchResultDto.Entry> ranks = webUrlRankClient.getByLocations(urls)
                .stream()
                .map(urlRank -> new ScoringSearchResultDto.Entry(urlRank.getLocation(), urlRank.getRank()))
                .collect(toList());

        return new ScoringSearchResultDto(ranks);
    }
}
