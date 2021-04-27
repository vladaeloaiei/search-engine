package com.aeloaiei.dissertation.search.engine.impl.search.searcher;

import java.util.Map;

public interface Searcher {
    public Map<String, Float> search(Map<String, Float> queryWords, Map<String, Map<String, Float>> webWords);
}
