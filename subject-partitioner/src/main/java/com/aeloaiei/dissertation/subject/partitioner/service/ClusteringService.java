package com.aeloaiei.dissertation.subject.partitioner.service;

import com.aeloaiei.dissertation.search.engine.api.clients.SearchEngineClient;
import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.subject.partitioner.config.Configuration;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebCategoryClient;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebDocumentSubjectClient;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebCategoryDto;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebDocumentSubjectDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ClusteringService {
    private static final Logger LOGGER = LogManager.getLogger(ClusteringService.class);

    @Autowired
    private Configuration config;
    @Autowired
    private SearchEngineClient searchEngineClient;
    @Autowired
    private WebCategoryClient webCategoryClient;
    @Autowired
    private WebDocumentSubjectClient webDocumentSubjectClient;

    public void run() throws Exception {
        Map<String, Set<String>> categoriesUrls = new HashMap<>();
        Map<String, Set<String>> categoriesKeyWords = loadCategoryKeyWords();

        for (int i = 0; i < config.maxIterationsCount; ++i) {
            Map<String, Set<String>> newCategoriesKeyWords = new HashMap<>();

            categoriesUrls = anIteration(categoriesKeyWords);
            LOGGER.info("Iteration " + i + " categories: " + new ObjectMapper().writeValueAsString(categoriesUrls));

            for (String category : categoriesUrls.keySet()) {
                if (!newCategoriesKeyWords.containsKey(category)) {
                    newCategoriesKeyWords.put(category, new HashSet<>());
                }

                List<String> newSubjects = webDocumentSubjectClient.getByLocations(categoriesUrls.get(category))
                        .stream()
                        .map(WebDocumentSubjectDto::getSubjects)
                        .flatMap(Set::stream)
                        .map(WebDocumentSubjectDto.Subject::getSubject)
                        .collect(toList());

                newCategoriesKeyWords.get(category).addAll(newSubjects);
            }

            if (!areDifferent(categoriesKeyWords, newCategoriesKeyWords)) {
                break;
            }

            categoriesKeyWords = newCategoriesKeyWords;
        }

        webCategoryClient.putAll(categoriesUrls.entrySet()
                .stream()
                .map(entry -> new WebCategoryDto(entry.getKey(), entry.getValue()))
                .collect(toList()));
    }

    private Map<String, Set<String>> loadCategoryKeyWords() {
        try {
            return Files.readAllLines(Paths.get(config.categoryKeywordsPath))
                    .stream()
                    .map(keywords -> Arrays.asList(keywords.split(" ")))
                    .filter(keywords -> !keywords.isEmpty())
                    .map(keywords -> new AbstractMap.SimpleEntry<String, Set<String>>(keywords.get(0), new HashSet<>(keywords)))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            LOGGER.error("Failed to load " + config.categoryKeywordsPath + " config file", e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, Set<String>> anIteration(Map<String, Set<String>> keyWords) {
        Map<String, Map<String, Float>> result = new HashMap<>();

        for (String category : keyWords.keySet()) {
            String query = String.join(" ", keyWords.get(category));

            result.put(category, searchEngineClient.scoringSearch(query).getScores()
                    .stream()
                    .collect(toMap(ScoringSearchResultDto.Entry::getUrl, ScoringSearchResultDto.Entry::getScore)));
        }

        Map<String, Pair<String, Float>> urls = new HashMap<>();

        for (String category : result.keySet()) {
            for (String url : result.get(category).keySet()) {
                if (urls.containsKey(url)) {
                    if (urls.get(url).getRight() < result.get(category).get(url)) {
                        urls.put(url, Pair.of(category, result.get(category).get(url)));
                    }
                } else {
                    urls.put(url, Pair.of(category, result.get(category).get(url)));
                }
            }
        }

        Map<String, Set<String>> categories = new HashMap<>();

        for (String url : urls.keySet()) {
            if (!categories.containsKey(urls.get(url).getLeft())) {
                categories.put(urls.get(url).getLeft(), new HashSet<>());
            }

            categories.get(urls.get(url).getLeft()).add(url);
        }

        return categories;
    }


    private boolean areDifferent(Map<String, Set<String>> keyWords, Map<String, Set<String>> newKeyWords) {
        for (String category : keyWords.keySet()) {
            if (!newKeyWords.containsKey(category)) {
                return true;
            } else {
                Set<String> initialWords = new HashSet<>(keyWords.get(category));
                Set<String> newWords = new HashSet<>(newKeyWords.get(category));
                float diff;

                initialWords.removeAll(newKeyWords.get(category));
                newWords.removeAll(keyWords.get(category));
                diff = (float) (initialWords.size() + newWords.size()) /
                        (keyWords.get(category).size() + newKeyWords.get(category).size());

                if (diff > config.maxIterationsDiff) {
                    LOGGER.info("Difference is bigger than max iteration diff (" + config.maxIterationsDiff * 100 + "%)");
                    LOGGER.info("Difference = " + diff * 100 + "%");
                    return true;
                }
            }
        }

        LOGGER.info("Difference is lower than max iteration diff (" + config.maxIterationsDiff * 100 + "%)");
        return false;
    }
}
