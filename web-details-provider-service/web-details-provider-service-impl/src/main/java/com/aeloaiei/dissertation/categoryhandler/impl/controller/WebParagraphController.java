package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebParagraphService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebParagraphDto;
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

@RequestMapping("/paragraph")
@RestController
public class WebParagraphController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebParagraphService webParagraphService;

    @PostMapping
    public ResponseEntity<List<WebParagraphDto>> getByIds(@RequestBody Collection<String> id) {
        List<WebParagraphDto> webParagraphs = webParagraphService.getByIds(id)
                .stream()
                .map(p -> modelMapper.map(p, WebParagraphDto.class))
                .collect(toList());

        return ResponseEntity.ok(webParagraphs);
    }
}
