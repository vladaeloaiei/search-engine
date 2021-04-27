package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebUrlRankDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebUrlRankClient", url = "${feign.client.web.details.provider.service.url}/urlrank")
public interface WebUrlRankClient {

    @PostMapping
    public List<WebUrlRankDto> getByLocations(@RequestBody Collection<String> locations);
}
