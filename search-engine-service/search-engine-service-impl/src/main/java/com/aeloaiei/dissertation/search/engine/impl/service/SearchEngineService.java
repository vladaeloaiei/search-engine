package com.aeloaiei.dissertation.search.engine.impl.service;

import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SliceSearchResultDto;
import com.aeloaiei.dissertation.search.engine.impl.config.Configuration;
import com.aeloaiei.dissertation.search.engine.impl.search.tokenizer.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto.Entry.comparingByScore;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class SearchEngineService {
    private static final int SLICE_SIZE = 50;

    @Autowired
    private ScoringWebSearchService scoringWebSearchService;
    @Autowired
    private RankingWebSearchService rankingWebSearchService;
    @Autowired
    private SearchResultWrapper searchResultWrapper;
    @Autowired
    private Tokenizer tokenizer;

    public SliceSearchResultDto sliceSearch(String query, int slice) {
        Map<String, Integer> queryTokens = getTokenizedWords(query);
        List<String> rawWords = getRawWords(query);

        return sliceSearch(queryTokens, rawWords, slice);
    }

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

    private SliceSearchResultDto sliceSearch(Map<String, Integer> queryTokens, List<String> rawWords, int slice) {
        List<String> urls = scoringPageRankingSearch(queryTokens).getScores()
                .stream()
                .sorted((a, b) -> comparingByScore().compare(b, a)) // Reverse sorting
                .map(ScoringSearchResultDto.Entry::getUrl)
                .collect(toList());

        int fromIndex = slice * SLICE_SIZE;
        int toIndex = (slice + 1) * SLICE_SIZE;
        int totalSlices = (int) Math.ceil((double) urls.size() / SLICE_SIZE);

        toIndex = Math.min(toIndex, urls.size());

        List<String> slicedUrls = urls.subList(fromIndex, toIndex);

        return new SliceSearchResultDto(searchResultWrapper.buildResult(slicedUrls, queryTokens.keySet(), rawWords), slice, totalSlices);
    }

    private SearchResultDto search(Map<String, Integer> queryTokens, List<String> rawWords) {
        List<String> urls = scoringPageRankingSearch(queryTokens).getScores()
                .stream()
                .sorted((a, b) -> comparingByScore().compare(b, a)) // Reverse sorting
                .map(ScoringSearchResultDto.Entry::getUrl)
                .collect(toList());

        return searchResultWrapper.buildResult(urls, queryTokens.keySet(), rawWords);
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
                        .peek(score -> score.setScore(score.getScore() * Configuration.SCORE_WEIGHT +
                                // If this url has no rank, the merged score will be based only on the search score
                                urlRanks.getOrDefault(score.getUrl(), score.getScore()) * Configuration.RANK_WEIGHT))
                        .collect(toList()));
    }

    private List<String> getRawWords(String query) {
        return asList(query.split("\\s+"));
    }

    private Map<String, Integer> getTokenizedWords(String query) {
        return tokenizer.extract(query).getRight();
    }
}
