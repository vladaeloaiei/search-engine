package com.aeloaiei.dissertation.searchengine;

import com.aeloaiei.dissertation.searchengine.impl.search.scoring.ScoreComputer;
import com.aeloaiei.dissertation.searchengine.impl.search.scoring.ScoreMatcher;
import com.aeloaiei.dissertation.searchengine.impl.search.tokenizer.LuceneTokenizer;
import com.aeloaiei.dissertation.searchengine.impl.search.tokenizer.Tokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SearchEngineApplication {
    private static final Logger LOGGER = LogManager.getLogger(SearchEngineApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SearchEngineApplication.class, args);
    }

    @Bean
    public ScoreComputer getScoringComputer() {
        return new ScoreMatcher();
    }

    @Bean
    public Tokenizer getTokenizer() {
        return new LuceneTokenizer();
    }
}
