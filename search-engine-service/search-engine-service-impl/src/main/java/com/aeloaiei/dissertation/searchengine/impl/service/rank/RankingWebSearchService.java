package com.aeloaiei.dissertation.searchengine.impl.service.rank;

import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.searchengine.impl.repository.WebUrlRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class RankingWebSearchService {
    @Autowired
    private WebUrlRankRepository webUrlRankRepository;

    public ScoringSearchResultDto rank(ScoringSearchResultDto scores) {
        List<ScoringSearchResultDto.Entry> ranks = scores.getScores().stream()
                .map(ScoringSearchResultDto.Entry::getUrl)
                .map(url -> webUrlRankRepository.findByLocation(url))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(urlRank -> new ScoringSearchResultDto.Entry(urlRank.getLocation(), urlRank.getRank()))
                .collect(toList());

        return new ScoringSearchResultDto(ranks);
    }
}
