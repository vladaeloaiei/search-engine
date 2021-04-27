package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.categoryhandler.impl.service.WebUrlRankService;
import com.aeloaiei.dissertation.web.details.provider.api.dto.WebUrlRankDto;
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

@RequestMapping("/urlrank")
@RestController
public class WebUrlRankController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebUrlRankService webUrlRankService;

    @PostMapping
    public ResponseEntity<List<WebUrlRankDto>> getByLocations(@RequestBody Collection<String> locations) {
        List<WebUrlRankDto> webUrlRanks = webUrlRankService.getByLocations(locations)
                .stream()
                .map(u -> modelMapper.map(u, WebUrlRankDto.class))
                .collect(toList());

        return ResponseEntity.ok(webUrlRanks);
    }
}
