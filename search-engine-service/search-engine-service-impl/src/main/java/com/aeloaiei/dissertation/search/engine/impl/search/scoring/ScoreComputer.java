package com.aeloaiei.dissertation.search.engine.impl.search.scoring;

import java.io.Serializable;
import java.util.Map;

public interface ScoreComputer extends Serializable {
    public float compute(Map<String, Float> query, Map<String, Float> document);
}
