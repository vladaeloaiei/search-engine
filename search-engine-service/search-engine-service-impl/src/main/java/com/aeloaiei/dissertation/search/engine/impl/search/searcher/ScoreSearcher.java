package com.aeloaiei.dissertation.search.engine.impl.search.searcher;

import com.aeloaiei.dissertation.search.engine.impl.search.scoring.ScoreComputer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoreSearcher implements Searcher {
    @Autowired
    private ScoreComputer scoreComputer;

    @Override
    public Map<String, Float> search(Map<String, Float> queryWords, Map<String, Map<String, Float>> webWords) {
        Map<String, Map<String, Float>> documents = reverseMapWebWordToDocument(webWords);
        Map<String, Float> documentScores = new HashMap<>();

        for (String document : documents.keySet()) {
            documentScores.put(document, scoreComputer.compute(queryWords, documents.get(document)));
        }

        return documentScores;
    }

    private Map<String, Map<String, Float>> reverseMapWebWordToDocument(Map<String, Map<String, Float>> webWords) {
        Map<String, Map<String, Float>> documents = new HashMap<>();

        for (String webWord : webWords.keySet()) {
            for (String url : webWords.get(webWord).keySet()) {
                if (!documents.containsKey(url)) {
                    documents.put(url, new HashMap<>());
                }

                documents.get(url).put(webWord, webWords.get(webWord).get(url));
            }
        }

        return documents;
    }
}
