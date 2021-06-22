package com.aeloaiei.dissertation.search.engine.impl.service;

import com.aeloaiei.dissertation.search.engine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebParagraphClient;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebTitleClient;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebWordClient;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebParagraphDto;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebTitleDto;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.aeloaiei.dissertation.search.engine.impl.config.Configuration.MAX_CONTENT_SIZE;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class SearchResultWrapper {

    @Autowired
    private WebWordClient webWordClient;
    @Autowired
    private WebTitleClient webTitleClient;
    @Autowired
    private WebParagraphClient webParagraphClient;

    public SearchResultDto buildResult(List<String> urls, Set<String> queryTokens, List<String> rawWords) {
        Set<String> setUrls = new HashSet<>(urls);

        List<SearchResultDto.Entry> urlParagraphs = webWordClient.getByWords(queryTokens)
                .stream()
                .flatMap(webWord -> mapAppearancesToResults(webWord.getAppearances(), setUrls))
                .collect(toList());

        Map<String, SearchResultDto.Entry> aggregatedParagraphs = aggregateParagraphs(urlParagraphs);

        // Keep the original order of urls
        List<SearchResultDto.Entry> orderedParagraphs = getOrderedParagraphs(urls, aggregatedParagraphs);

        return new SearchResultDto(rawWords, orderedParagraphs);
    }

    private Stream<SearchResultDto.Entry> mapAppearancesToResults(Set<WebWordDto.Appearance> appearances, Set<String> urls) {
        return appearances.stream()
                .filter(appearance -> urls.contains(appearance.getLocation()))
                .map(this::buildResultEntryFor);
    }

    private SearchResultDto.Entry buildResultEntryFor(WebWordDto.Appearance appearance) {
        String title = webTitleClient.getByLocations(singletonList(appearance.getLocation()))
                .stream()
                .findFirst()
                .map(WebTitleDto::getTitle)
                .orElse("");

        Set<String> paragraphs = webParagraphClient.getByIds(appearance.getParagraphsIds())
                .stream()
                .map(WebParagraphDto::getContent)
                .map(this::limitParagraphContent)
                .collect(toSet());

        return new SearchResultDto.Entry(appearance.getLocation(), title, paragraphs);
    }

    private Map<String, SearchResultDto.Entry> aggregateParagraphs(List<SearchResultDto.Entry> urlParagraphs) {
        Map<String, SearchResultDto.Entry> paragraphs = new HashMap<>();

        for (SearchResultDto.Entry entry : urlParagraphs) {
            if (paragraphs.containsKey(entry.getUrl())) {
                paragraphs.get(entry.getUrl()).getParagraphs().addAll(entry.getParagraphs());
            } else {
                paragraphs.put(entry.getUrl(), entry);
            }
        }

        return paragraphs;
    }

    private List<SearchResultDto.Entry> getOrderedParagraphs(List<String> urls, Map<String, SearchResultDto.Entry> paragraphs) {
        return urls.stream()
                .map(url -> paragraphs.getOrDefault(url, new SearchResultDto.Entry(url)))
                .collect(toList());
    }

    private String limitParagraphContent(String intro) {
        int endIndex = Math.min(MAX_CONTENT_SIZE, intro.length());
        return intro.substring(0, endIndex) + " ...";
    }
}
