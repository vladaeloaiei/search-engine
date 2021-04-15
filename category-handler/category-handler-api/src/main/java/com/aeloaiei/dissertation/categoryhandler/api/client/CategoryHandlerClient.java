package com.aeloaiei.dissertation.categoryhandler.api.client;

import com.aeloaiei.dissertation.categoryhandler.api.dto.WebCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "CategoryHandlerClient", url = "http://localhost:10002/category")
public interface CategoryHandlerClient {

    @PutMapping
    public void putAll(@RequestBody List<WebCategoryDto> webCategoryDtos);

    @GetMapping
    public List<WebCategoryDto> getAll();

    @GetMapping
    public WebCategoryDto getByCategory(@RequestParam String category);
}
