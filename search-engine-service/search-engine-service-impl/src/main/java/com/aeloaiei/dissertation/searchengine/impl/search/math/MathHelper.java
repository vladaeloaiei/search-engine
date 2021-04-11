package com.aeloaiei.dissertation.searchengine.impl.search.math;

import java.util.Map;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class MathHelper {
    public static float module(Map<String, Float> vector) {
        float module = 0;

        for (Float value : vector.values()) {
            module += value * value;
        }

        return (float) sqrt(module);
    }

    public static float dotProduct(Map<String, Float> vector1, Map<String, Float> vector2) {
        float dotProduct = 0;

        for (String key : vector1.keySet()) {
            if (vector2.containsKey(key)) {
                dotProduct += vector1.get(key) * vector2.get(key);
            }
        }

        return dotProduct;
    }

    public static float logNormalizedTermFrequency(int termCount, int documentSize) {
        return (float) log(1 + (float) termCount / documentSize);
    }

    public static float inverseDocumentFrequencySmooth(int total, int appearances) {
        return (float) log((float) total / (1 + appearances)) + 1;
    }
}
