package com.aeloaiei.dissertation.subject.partitioner.config;

import org.springframework.beans.factory.annotation.Value;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${config.max.iterations.count}")
    public int maxIterationsCount;

    @Value("${config.max.iterations.diff}")
    public float maxIterationsDiff;

    @Value("${config.category.keywords.path}")
    public String categoryKeywordsPath;
}
