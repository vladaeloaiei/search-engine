package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebTitleService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebTitleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/title")
@RestController
public class WebTitleController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebTitleService webTitleService;

    @PostMapping
    public ResponseEntity<List<WebTitleDto>> getByLocations(@RequestBody Collection<String> locations) {
        List<WebTitleDto> webTitles = webTitleService.getByLocations(locations)
                .stream()
                .map(t -> modelMapper.map(t, WebTitleDto.class))
                .collect(toList());

        return ResponseEntity.ok(webTitles);
    }
}
