package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "WebCategoryClient", url = "${feign.client.web.details.provider.service.url}/category")
public interface WebCategoryClient {

    @PutMapping
    public void putAll(@RequestBody List<WebCategoryDto> webCategoryDtos);

    @GetMapping
    public List<WebCategoryDto> getAll();
}
