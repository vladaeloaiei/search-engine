package com.aeloaiei.dissertation.searchengine.impl.search.scoring;

import java.io.Serializable;
import java.util.Map;

public interface ScoreComputer extends Serializable {
    public float compute(Map<String, Float> query, Map<String, Float> document);
}
