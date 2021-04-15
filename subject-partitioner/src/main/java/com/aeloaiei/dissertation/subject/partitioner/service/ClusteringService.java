package com.aeloaiei.dissertation.subject.partitioner.service;

import com.aeloaiei.dissertation.categoryhandler.api.client.CategoryHandlerClient;
import com.aeloaiei.dissertation.categoryhandler.api.dto.WebCategoryDto;
import com.aeloaiei.dissertation.searchengine.api.clients.SearchEngineClient;
import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.subject.partitioner.model.WebDocumentSubject;
import com.aeloaiei.dissertation.subject.partitioner.repository.WebDocumentSubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ClusteringService {
    private static final Logger LOGGER = LogManager.getLogger(ClusteringService.class);

    private static final Set<String> SPORT_KEYWORDS = new HashSet<>(asList("sport", "football", "cricket", "tennis", "cycling", "game", "league"));
    private static final Set<String> SCIENCE_KEYWORDS = new HashSet<>(asList("science", "study", "sea", "electric", "documentary", "space", "nasa"));
    private static final Set<String> MUSIC_KEYWORDS = new HashSet<>(asList("music", "theme", "mix", "song", "sound", "rapper", "voice"));
    private static final Set<String> WEATHER_KEYWORDS = new HashSet<>(asList("weather", "word", "location", "city", "climate", "wind", "air"));

    private static final int MAX_ITERATION_COUNT = 1000;
    private static final float MAX_ITERATION_DIFF = 0.05F; // 5%

    @Autowired
    private CategoryHandlerClient categoryHandlerClient;
    @Autowired
    private SearchEngineClient searchEngineClient;
    @Autowired
    private WebDocumentSubjectRepository webDocumentSubjectRepository;

    public void run() throws Exception {
        Map<String, Set<String>> categoriesUrls = new HashMap<>();
        Map<String, Set<String>> categoriesKeyWords = new HashMap<String, Set<String>>() {{
            put("SPORT", SPORT_KEYWORDS);
            put("SCIENCE", SCIENCE_KEYWORDS);
            put("MUSIC", MUSIC_KEYWORDS);
            put("WEATHER", WEATHER_KEYWORDS);
        }};

        for (int i = 0; i < MAX_ITERATION_COUNT; ++i) {
            Map<String, Set<String>> newCategoriesKeyWords = new HashMap<>();

            categoriesUrls = anIteration(categoriesKeyWords);
            LOGGER.info("Iteration " + i + " categories: " + new ObjectMapper().writeValueAsString(categoriesUrls));

            for (String category : categoriesUrls.keySet()) {
                Set<String> newSubjects = new HashSet<>();

                for (String url : categoriesUrls.get(category)) {
                    if (!newCategoriesKeyWords.containsKey(category)) {
                        newCategoriesKeyWords.put(category, new HashSet<>());
                    }

                    webDocumentSubjectRepository.findByLocation(url)
                            .map(WebDocumentSubject::getSubjects)
                            .ifPresent(subjects -> subjects.forEach(subject -> newSubjects.add(subject.getSubject())));
                }

                newCategoriesKeyWords.get(category).addAll(newSubjects);
            }

            if (!areDifferent(categoriesKeyWords, newCategoriesKeyWords)) {
                break;
            }

            categoriesKeyWords = newCategoriesKeyWords;
        }

        categoryHandlerClient.putAll(categoriesUrls.entrySet()
                .stream()
                .map(entry -> new WebCategoryDto(entry.getKey(), entry.getValue()))
                .collect(toList()));
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

                if (diff > MAX_ITERATION_DIFF) {
                    LOGGER.info("Difference is bigger than max iteration diff (" + MAX_ITERATION_DIFF * 100 + "%)");
                    LOGGER.info("Difference = " + diff * 100 + "%");
                    return true;
                }
            }
        }

        LOGGER.info("Difference is lower than max iteration diff (" + MAX_ITERATION_DIFF * 100 + "%)");
        return false;
    }
}
