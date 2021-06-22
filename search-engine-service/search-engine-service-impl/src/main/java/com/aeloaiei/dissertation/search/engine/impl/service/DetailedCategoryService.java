package com.aeloaiei.dissertation.search.engine.impl.service;

import com.aeloaiei.dissertation.search.engine.api.dto.DetailedCategoryDto;
import com.aeloaiei.dissertation.search.engine.impl.config.Configuration;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebCategoryClient;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebIntroClient;
import com.aeloaiei.dissertation.web.details.provider.api.client.WebTitleClient;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebCategoryDto;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebIntroDto;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebTitleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.aeloaiei.dissertation.search.engine.impl.config.Configuration.MAX_CONTENT_SIZE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class DetailedCategoryService {

    @Autowired
    private WebTitleClient webTitleClient;
    @Autowired
    private WebIntroClient webIntroClient;
    @Autowired
    private WebCategoryClient webCategoryClient;

    public List<DetailedCategoryDto> getAll() {
        List<WebCategoryDto> categories = webCategoryClient.getAll();
        List<String> locations = categories.stream()
                .flatMap(c -> c.getUrls().stream())
                .collect(toList());
        Map<String, String> titles = webTitleClient.getByLocations(locations)
                .stream()
                .collect(toMap(WebTitleDto::getLocation, WebTitleDto::getTitle));
        Map<String, String> intros = webIntroClient.getByLocations(locations)
                .stream()
                .collect(toMap(WebIntroDto::getLocation, WebIntroDto::getContent));

        return categories.stream()
                .map(category -> new DetailedCategoryDto(category.getName(), buildDetailedUrls(category.getUrls(), titles, intros)))
                .collect(toList());
    }

    private Set<DetailedCategoryDto.Page> buildDetailedUrls(Set<String> urls,
                                                            Map<String, String> titles,
                                                            Map<String, String> intros) {
        return urls.stream()
                .map(url -> new DetailedCategoryDto.Page(
                        url,
                        titles.getOrDefault(url, ""),
                        limitIntroContent(intros.getOrDefault(url, ""))))
                .collect(toSet());
    }

    private String limitIntroContent(String intro) {
        int endIndex = Math.min(MAX_CONTENT_SIZE, intro.length());
        return intro.substring(0, endIndex) + " ...";
    }
}
