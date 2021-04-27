package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebWordService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebWordDto;
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

@RequestMapping("/word")
@RestController
public class WebWordController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebWordService webWordService;

    @PostMapping
    public ResponseEntity<List<WebWordDto>> getByWords(@RequestBody Collection<String> words) {
        List<WebWordDto> webWords = webWordService.getByWords(words)
                .stream()
                .map(w -> modelMapper.map(w, WebWordDto.class))
                .collect(toList());

        return ResponseEntity.ok(webWords);
    }
}
