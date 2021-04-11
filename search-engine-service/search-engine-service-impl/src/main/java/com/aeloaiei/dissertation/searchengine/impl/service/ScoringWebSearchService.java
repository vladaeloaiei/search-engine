package com.aeloaiei.dissertation.searchengine.impl.service;

import com.aeloaiei.dissertation.searchengine.impl.model.WebWord;
import com.aeloaiei.dissertation.searchengine.impl.repository.WebWordRepository;
import com.aeloaiei.dissertation.searchengine.impl.search.tokenizer.Tokenizer;
import com.aeloaiei.dissertation.searchengine.impl.service.searcher.ScoreSearcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aeloaiei.dissertation.searchengine.impl.search.math.MathHelper.logNormalizedTermFrequency;
import static java.util.stream.Collectors.toMap;

@Service
public class ScoringWebSearchService {
    private static final Logger LOGGER = LogManager.getLogger(ScoringWebSearchService.class);

    @Autowired
    private WebWordRepository webWordRepository;
    @Autowired
    private ScoreSearcher scoreSearcher;
    @Autowired
    private Tokenizer tokenizer;

    public Map<String, Float> search(String query) {
        LOGGER.info("Received for searching query: " + query);

        Map<String, Integer> tokens = tokenizer.extract(query).getRight();

        Set<WebWord> webWords = tokens.keySet().stream()
                .map(token -> webWordRepository.findByWord(token))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        Map<String, Float> webWordsIdf = webWords.stream()
                .collect(toMap(WebWord::getWord, WebWord::getInverseDocumentFrequencySmooth));

        Map<String, Map<String, Float>> webWordsScores = webWords.stream()
                .map(this::computeScoresForWebWord)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Float> queryWordsScores = tokens.entrySet()
                .stream()
                .map(word -> mapWordCountToScore(word, webWordsIdf))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, Float> searchResult = scoreSearcher.search(queryWordsScores, webWordsScores);

        LOGGER.info("Search result: " + searchResult);
        return searchResult;
    }

    private Map.Entry<String, Map<String, Float>> computeScoresForWebWord(WebWord webWord) {
        Map<String, Float> scores = webWord.getAppearances().stream()
                .map(appearance -> computeScoreForAppearance(appearance, webWord.getInverseDocumentFrequencySmooth()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new AbstractMap.SimpleEntry<>(webWord.getWord(), scores);
    }

    private Map.Entry<String, Float> computeScoreForAppearance(WebWord.Appearance appearance, Float webWordIdf) {
        return new AbstractMap.SimpleEntry<>(appearance.getLocation(), appearance.getLogNormalizedTermFrequency() * webWordIdf);
    }

    private Map.Entry<String, Float> mapWordCountToScore(Map.Entry<String, Integer> word, Map<String, Float> webWordsIdf) {
        float webWordIdf = 0;

        if (webWordsIdf.containsKey(word.getKey())) {
            webWordIdf = webWordsIdf.get(word.getKey());
        }

        return new AbstractMap.SimpleEntry<>(word.getKey(),
                logNormalizedTermFrequency(word.getValue(), webWordsIdf.size()) * webWordIdf);
    }
}
