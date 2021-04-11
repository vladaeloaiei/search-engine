package com.aeloaiei.dissertation.subject.partitioner.temp.service;

import com.aeloaiei.dissertation.searchengine.api.clients.SearchEngineClient;
import com.aeloaiei.dissertation.searchengine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.subject.partitioner.temp.model.WebDocumentSubject;
import com.aeloaiei.dissertation.subject.partitioner.temp.repository.WebDocumentSubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class ClusteringService {
    private static final Logger LOGGER = LogManager.getLogger(ClusteringService.class);

    private static final Set<String> SPORT_KEYWORDS = new HashSet<>(asList("sport", "football", "cricket", "tennis", "cycling", "game", "league"));
    private static final Set<String> SCIENCE_KEYWORDS = new HashSet<>(asList("science", "study", "sea", "electric", "documentary", "space", "nasa"));
    private static final Set<String> MUSIC_KEYWORDS = new HashSet<>(asList("music", "theme", "mix", "song", "sound", "rapper", "voice"));
    private static final Set<String> WEATHER_KEYWORDS = new HashSet<>(asList("weather", "word", "location", "city", "climate", "wind", "air"));

    @Autowired
    private SearchEngineClient searchEngineClient;
    @Autowired
    private WebDocumentSubjectRepository webDocumentSubjectRepository;

    @PostConstruct
    public void ceva() throws Exception {
        Map<Category, Set<String>> keyWords = new HashMap<>();

        keyWords.put(Category.SPORT, SPORT_KEYWORDS);
        keyWords.put(Category.SCIENCE, SCIENCE_KEYWORDS);
        keyWords.put(Category.MUSIC, MUSIC_KEYWORDS);
        keyWords.put(Category.WEATHER, WEATHER_KEYWORDS);

        for (int i = 0; i < 1000; ++i) {
            Map<Category, Set<String>> categories = anIteration(keyWords);
            Map<Category, Set<String>> newKeyWords = new HashMap<>();

            // TODO ket top x keywords by count
            // TODO aduna toate counturile tuturor keywordsurilor si alege top x (1k)


            LOGGER.info("CEVA: " + new ObjectMapper().writeValueAsString(categories));

            for (Category category : categories.keySet()) {
                Map<String, Integer> newSubjects = new HashMap<>();

                for (String url : categories.get(category)) {
                    if (!newKeyWords.containsKey(category)) {
                        newKeyWords.put(category, new HashSet<>());
                    }

                    webDocumentSubjectRepository.findByLocation(url)
                            .map(WebDocumentSubject::getSubjects)
                            .ifPresent(s -> s.forEach(subject -> accumulateSubjects(newSubjects, subject)));
                }

                Set<String> importantSubjects = newSubjects.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .limit(1000)
                        .map(Map.Entry::getKey)
                        .collect(toSet());

                newKeyWords.get(category).addAll(importantSubjects);
            }

            if (!areDifferent(keyWords, newKeyWords)) {
                break;
            }

            keyWords = newKeyWords;
        }
    }

    private Map<Category, Set<String>> anIteration(Map<Category, Set<String>> keyWords) {
        Map<Category, Map<String, Float>> result = new HashMap<>();

        for (Category category : keyWords.keySet()) {
            String query = String.join(" ", keyWords.get(category));

            result.put(category, searchEngineClient.scoringSearch(query).getScores()
                    .stream()
                    .collect(toMap(ScoringSearchResultDto.Entry::getUrl, ScoringSearchResultDto.Entry::getScore)));
        }

        Map<String, Pair<Category, Float>> urls = new HashMap<>();

        for (Category category : result.keySet()) {
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

        Map<Category, Set<String>> categories = new HashMap<>();

        for (String url : urls.keySet()) {
            if (!categories.containsKey(urls.get(url).getLeft())) {
                categories.put(urls.get(url).getLeft(), new HashSet<>());
            }

            categories.get(urls.get(url).getLeft()).add(url);
        }

        return categories;
    }


    private boolean areDifferent(Map<Category, Set<String>> keyWords, Map<Category, Set<String>> newKeyWords) {
        for (Category category : keyWords.keySet()) {
            if (!newKeyWords.containsKey(category)) {
                return true;
            } else {
                Set<String> initialWords = new HashSet<>(keyWords.get(category));
                Set<String> newWords = new HashSet<>(newKeyWords.get(category));
                float diff = 0;

                initialWords.removeAll(newKeyWords.get(category));
                newWords.removeAll(keyWords.get(category));
                diff = 100F * (initialWords.size() + newWords.size()) /
                        (keyWords.get(category).size() + newKeyWords.get(category).size());

                if (diff > 5) {
                    LOGGER.info("Diff bigger than 5% : " + diff + "%");
                    return true;
                }
            }
        }

        LOGGER.info("Diff lower than 5%");
        return false;
    }

    private void accumulateSubjects(Map<String, Integer> subjects, WebDocumentSubject.Subject subject) {
        int count = subjects.getOrDefault(subject.getSubject(), 0) + subject.getCount();

        subjects.put(subject.getSubject(), count);
    }

    enum Category {
        SPORT,
        SCIENCE,
        MUSIC,
        WEATHER
    }
}
