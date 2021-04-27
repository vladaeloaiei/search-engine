package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebIntroService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebIntroDto;
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

@RequestMapping("/intro")
@RestController
public class WebIntroController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebIntroService webIntroService;

    @PostMapping
    public ResponseEntity<List<WebIntroDto>> getByLocations(@RequestBody Collection<String> locations) {
        List<WebIntroDto> webIntros = webIntroService.getByLocations(locations)
                .stream()
                .map(t -> modelMapper.map(t, WebIntroDto.class))
                .collect(toList());

        return ResponseEntity.ok(webIntros);
    }
}
