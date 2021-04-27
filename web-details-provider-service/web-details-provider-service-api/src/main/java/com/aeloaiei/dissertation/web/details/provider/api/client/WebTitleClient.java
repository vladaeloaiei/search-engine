package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebTitleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebTitleClient", url = "${feign.client.web.details.provider.service.url}/title")
public interface WebTitleClient {

    @PostMapping()
    public List<WebTitleDto> getByLocations(@RequestBody Collection<String> locations);
}
