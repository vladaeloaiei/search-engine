package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebParagraphDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebParagraphClient", url = "${feign.client.web.details.provider.service.url}/paragraph")
public interface WebParagraphClient {

    @PostMapping
    public List<WebParagraphDto> getByIds(@RequestBody Collection<String> id);
}
