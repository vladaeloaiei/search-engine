package com.aeloaiei.dissertation.searchengine.impl.service.searcher;

import java.util.Map;

public interface Searcher {
    public Map<String, Float> search(Map<String, Float> queryWords, Map<String, Map<String, Float>> webWords);
}
