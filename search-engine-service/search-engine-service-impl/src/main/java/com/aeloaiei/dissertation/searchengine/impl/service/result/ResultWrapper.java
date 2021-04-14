package com.aeloaiei.dissertation.searchengine.impl.service.result;

import com.aeloaiei.dissertation.searchengine.api.dto.SearchResultDto;
import com.aeloaiei.dissertation.searchengine.impl.model.WebParagraph;
import com.aeloaiei.dissertation.searchengine.impl.model.WebTitle;
import com.aeloaiei.dissertation.searchengine.impl.model.WebWord;
import com.aeloaiei.dissertation.searchengine.impl.repository.WebParagraphRepository;
import com.aeloaiei.dissertation.searchengine.impl.repository.WebTitleRepository;
import com.aeloaiei.dissertation.searchengine.impl.repository.WebWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class ResultWrapper {
    @Autowired
    private WebWordRepository webWordRepository;
    @Autowired
    private WebParagraphRepository webParagraphRepository;
    @Autowired
    private WebTitleRepository webTitleRepository;

    public SearchResultDto buildResult(List<String> urls, Set<String> queryTokens, List<String> rawWords) {
        Set<String> setUrls = new HashSet<>(urls);

        List<SearchResultDto.Entry> urlParagraphs = queryTokens.stream()
                .map(token -> webWordRepository.findByWord(token))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(webWord -> mapAppearancesToResults(webWord.getAppearances(), setUrls))
                .collect(toList());

        Map<String, SearchResultDto.Entry> aggregatedParagraphs = aggregateParagraphs(urlParagraphs);

        // Keep the original order of urls
        List<SearchResultDto.Entry> orderedParagraphs = getOrderedParagraphs(urls, aggregatedParagraphs);

        return new SearchResultDto(rawWords, orderedParagraphs);
    }

    private Stream<SearchResultDto.Entry> mapAppearancesToResults(Set<WebWord.Appearance> appearances, Set<String> urls) {
        return appearances.stream()
                .filter(appearance -> urls.contains(appearance.getLocation()))
                .map(this::buildResultEntryFor);
    }

    private SearchResultDto.Entry buildResultEntryFor(WebWord.Appearance appearance) {
        String title = webTitleRepository.findByLocation(appearance.getLocation())
                .map(WebTitle::getTitle)
                .orElse("");
        Set<String> paragraphs = appearance.getParagraphsIds()
                .stream()
                .map(webParagraphRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(WebParagraph::getContent)
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
}
