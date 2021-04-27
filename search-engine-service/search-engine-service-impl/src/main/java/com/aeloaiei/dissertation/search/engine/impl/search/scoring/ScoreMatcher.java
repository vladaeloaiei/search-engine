package com.aeloaiei.dissertation.search.engine.impl.search.scoring;

import java.util.Map;

public class ScoreMatcher implements ScoreComputer {
    @Override
    public float compute(Map<String, Float> query, Map<String, Float> document) {
        float score = 0;

        for (String word : query.keySet()) {
            if (document.containsKey(word)) {
                score += document.get(word);
            }
        }

        return score;
    }
}
