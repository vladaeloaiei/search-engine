package com.aeloaiei.dissertation.search.engine.impl.search.tokenizer;

import org.modelmapper.internal.Pair;

import java.io.Serializable;
import java.util.Map;

public interface Tokenizer extends Serializable {
    /**
     * Extract tokens from a given text
     *
     * @param text Input text
     * @return a {@link org.modelmapper.internal.Pair} of number of total words from the given text and a map of tokens and their appearance
     */
    public Pair<Integer, Map<String, Integer>> extract(String text);
}
