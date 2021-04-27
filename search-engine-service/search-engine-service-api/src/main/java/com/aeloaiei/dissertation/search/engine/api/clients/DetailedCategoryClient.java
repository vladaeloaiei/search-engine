package com.aeloaiei.dissertation.search.engine.api.clients;

import com.aeloaiei.dissertation.search.engine.api.dto.DetailedCategoryDto;
import com.aeloaiei.dissertation.search.engine.api.dto.ScoringSearchResultDto;
import com.aeloaiei.dissertation.search.engine.api.dto.SearchResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "DetailedCategoryClient", url = "${feign.client.search.engine.service.url}/detailedcategory")
public interface DetailedCategoryClient {
    @GetMapping
    public List<DetailedCategoryDto> getAll();
}
