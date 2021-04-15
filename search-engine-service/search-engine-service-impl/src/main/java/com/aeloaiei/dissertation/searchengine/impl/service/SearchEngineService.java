package com.aeloaiei.dissertation.searchengine.impl.service;

import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.searchengine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.searchengine.impl.search.tokenizer.Tokenizer;
import com.aeloaiei.dissertation.searchengine.impl.service.rank.RankingWebSearchService;
import com.aeloaiei.dissertation.searchengine.impl.service.result.ResultWrapper;
import com.aeloaiei.dissertation.searchengine.impl.service.score.ScoringWebSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto.Entry.comparingByScore;
import static com.aeloaiei.dissertation.searchengine.impl.config.Configuration.RANK_WEIGHT;
import static com.aeloaiei.dissertation.searchengine.impl.config.Configuration.SCORE_WEIGHT;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class SearchEngineService {
    @Autowired
    private ScoringWebSearchService scoringWebSearchService;
    @Autowired
    private RankingWebSearchService rankingWebSearchService;
    @Autowired
    private ResultWrapper resultWrapper;
    @Autowired
    private Tokenizer tokenizer;

    public SearchResultDto search(String query) {
        Map<String, Integer> queryTokens = getTokenizedWords(query);
        List<String> rawWords = getRawWords(query);

        return search(queryTokens, rawWords);
    }

    public ScoringSearchResultDto scoringSearch(String query) {
        Map<String, Integer> queryTokens = getTokenizedWords(query);

        return scoringSearch(queryTokens);
    }

    public ScoringSearchResultDto scoringPageRankingSearch(String query) {
        Map<String, Integer> queryTokens = getTokenizedWords(query);

        return scoringPageRankingSearch(queryTokens);
    }

    private SearchResultDto search(Map<String, Integer> queryTokens, List<String> rawWords) {
        List<String> urls = scoringPageRankingSearch(queryTokens).getScores()
                .stream()
                // Reverse sorting
                .sorted((a, b) -> comparingByScore().compare(b, a))
                .map(ScoringSearchResultDto.Entry::getUrl)
                .collect(toList());

        return resultWrapper.buildResult(urls, queryTokens.keySet(), rawWords);
    }

    private ScoringSearchResultDto scoringSearch(Map<String, Integer> queryTokens) {
        return scoringWebSearchService.search(queryTokens);
    }

    private ScoringSearchResultDto scoringPageRankingSearch(Map<String, Integer> queryTokens) {
        ScoringSearchResultDto scores = scoringSearch(queryTokens);
        ScoringSearchResultDto ranks = rankingWebSearchService.rank(scores);

        return mergeRankScore(scores, ranks);
    }

    private ScoringSearchResultDto mergeRankScore(ScoringSearchResultDto scores, ScoringSearchResultDto ranks) {
        Map<String, Float> urlRanks = ranks.getScores()
                .stream()
                .collect(toMap(ScoringSearchResultDto.Entry::getUrl, ScoringSearchResultDto.Entry::getScore));

        return new ScoringSearchResultDto(
                scores.getScores()
                        .stream()
                        .peek(score -> score.setScore(score.getScore() * SCORE_WEIGHT +
                                // If this url has no rank, the merged score will be based only on the search score
                                urlRanks.getOrDefault(score.getUrl(), score.getScore()) * RANK_WEIGHT))
                        .collect(toList()));
    }

    private List<String> getRawWords(String query) {
        return asList(query.split("\\s+"));
    }

    private Map<String, Integer> getTokenizedWords(String query) {
        return tokenizer.extract(query).getRight();
    }
}
