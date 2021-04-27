package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebIntroDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebIntroClient", url = "${feign.client.web.details.provider.service.url}/intro")
public interface WebIntroClient {

    @PostMapping
    public List<WebIntroDto> getByLocations(@RequestBody Collection<String> locations);
}
