package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebWordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebWordClient", url = "${feign.client.web.details.provider.service.url}/word")
public interface WebWordClient {

    @PostMapping
    public List<WebWordDto> getByWords(@RequestBody Collection<String> words);
}
