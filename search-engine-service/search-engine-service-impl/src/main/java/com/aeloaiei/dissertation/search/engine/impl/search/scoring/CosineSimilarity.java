package com.aeloaiei.dissertation.search.engine.impl.search.scoring;

import com.aeloaiei.dissertation.search.engine.impl.search.math.MathHelper;

import java.util.Map;

public class CosineSimilarity implements ScoreComputer {
    @Override
    public float compute(Map<String, Float> query, Map<String, Float> document) {
        return MathHelper.dotProduct(query, document) / (MathHelper.module(query) * MathHelper.module(document));
    }
}
