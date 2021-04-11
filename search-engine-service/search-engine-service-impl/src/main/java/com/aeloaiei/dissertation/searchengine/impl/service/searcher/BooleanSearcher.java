package com.aeloaiei.dissertation.searchengine.impl.service.searcher;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BooleanSearcher implements Searcher {

    @Override
    public Map<String, Float> search(Map<String, Float> queryWords, Map<String, Map<String, Float>> webWords) {
        Map<String, Float> urlScores = initScores(webWords);

        computeScores(urlScores, webWords);

        return urlScores;
    }

    private Map<String, Float> initScores(Map<String, Map<String, Float>> webWords) {
        Map<String, Float> urlScores = new HashMap<>();

        for (String webWord : webWords.keySet()) {
            for (String url : webWords.get(webWord).keySet()) {
                urlScores.put(url, 0F);
            }
        }

        return urlScores;
    }

    private void computeScores(Map<String, Float> urlScores, Map<String, Map<String, Float>> webWords) {
        for (String url : urlScores.keySet()) {
            for (String webWord : webWords.keySet()) {
                if (webWords.get(webWord).containsKey(url)) {
                    float currentScore = urlScores.getOrDefault(url, 0F);

                    urlScores.put(url, currentScore + 1F);
                }
            }
        }
    }
}
