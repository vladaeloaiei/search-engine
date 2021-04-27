package com.aeloaiei.dissertation.search.engine.impl.service;

import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.search.engine.impl.search.math.MathHelper;
import com.aeloaiei.dissertation.search.engine.impl.search.searcher.ScoreSearcher;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebWordClient;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebWordDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ScoringWebSearchService {
    private static final Logger LOGGER = LogManager.getLogger(ScoringWebSearchService.class);

    @Autowired
    private WebWordClient webWordClient;
    @Autowired
    private ScoreSearcher scoreSearcher;

    public ScoringSearchResultDto search(Map<String, Integer> tokens) {
        LOGGER.info("Received for searching query tokens: " + tokens.toString());

        List<WebWordDto> webWords = webWordClient.getByWords(tokens.keySet());

        Map<String, Float> webWordsIdf = webWords.stream()
                .collect(toMap(WebWordDto::getWord, WebWordDto::getInverseDocumentFrequencySmooth));

        Map<String, Map<String, Float>> webWordsScores = webWords.stream()
                .map(this::computeScoresForWebWord)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Float> queryWordsScores = tokens.entrySet()
                .stream()
                .map(word -> mapWordCountToScore(word, webWordsIdf))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Float> searchResult = scoreSearcher.search(queryWordsScores, webWordsScores);

        LOGGER.info("Search result: " + searchResult);

        return new ScoringSearchResultDto(searchResult.entrySet()
                .stream()
                .map(entry -> new ScoringSearchResultDto.Entry(entry.getKey(), entry.getValue()))
                .collect(toList()));
    }

    private Map.Entry<String, Map<String, Float>> computeScoresForWebWord(WebWordDto webWord) {
        Map<String, Float> scores = webWord.getAppearances().stream()
                .map(appearance -> computeScoreForAppearance(appearance, webWord.getInverseDocumentFrequencySmooth()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new AbstractMap.SimpleEntry<>(webWord.getWord(), scores);
    }

    private Map.Entry<String, Float> computeScoreForAppearance(WebWordDto.Appearance appearance, Float webWordIdf) {
        return new AbstractMap.SimpleEntry<>(appearance.getLocation(), appearance.getLogNormalizedTermFrequency() * webWordIdf);
    }

    private Map.Entry<String, Float> mapWordCountToScore(Map.Entry<String, Integer> word, Map<String, Float> webWordsIdf) {
        float webWordIdf = 0;

        if (webWordsIdf.containsKey(word.getKey())) {
            webWordIdf = webWordsIdf.get(word.getKey());
        }

        return new AbstractMap.SimpleEntry<>(word.getKey(),
                MathHelper.logNormalizedTermFrequency(word.getValue(), webWordsIdf.size()) * webWordIdf);
    }
}
