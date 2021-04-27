package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebDocumentSubjectService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebDocumentSubjectDto;
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

@RequestMapping("/documentsubject")
@RestController
public class WebDocumentSubjectController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebDocumentSubjectService webDocumentSubjectService;

    @PostMapping
    public ResponseEntity<List<WebDocumentSubjectDto>> getByLocations(@RequestBody Collection<String> locations) {
        List<WebDocumentSubjectDto> webDocumentSubjects = webDocumentSubjectService.getByLocations(locations)
                .stream()
                .map(s -> modelMapper.map(s, WebDocumentSubjectDto.class))
                .collect(toList());

        return ResponseEntity.ok(webDocumentSubjects);
    }
}
