package com.aeloaiei.dissertation.web.details.provider.api.client;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebDocumentSubjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "WebDocumentSubjectClient", url = "${feign.client.web.details.provider.service.url}/documentsubject")
public interface WebDocumentSubjectClient {

    @PostMapping
    public List<WebDocumentSubjectDto> getByLocations(@RequestBody Collection<String> locations);
}
