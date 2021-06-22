package com.aeloaiei.dissertation.categoryhandler.impl.controller;

import com.aeloaiei.dissertation.web.details.provider.api.dto.WebCategoryDto;
import com.aeloaiei.dissertation.categoryhandler.impl.model.WebCategory;
import com.aeloaiei.dissertation.categoryhandler.impl.service.WebCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/category")
@RestController
public class WebCategoryController {
    @Autowired
    private WebCategoryService webCategoryService;
    @Autowired
    private ModelMapper modelMapper;

    @PutMapping
    public ResponseEntity<WebCategory> putAll(@RequestBody List<WebCategoryDto> webCategoryDtos) {
        List<WebCategory> webCategories = webCategoryDtos.stream()
                .map(c -> modelMapper.map(c, WebCategory.class))
                .collect(toList());

        webCategoryService.putAll(webCategories);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebCategoryDto>> getAll() {
        List<WebCategoryDto> webCategoryDtos = webCategoryService.getAll()
                .stream()
                .map(c -> modelMapper.map(c, WebCategoryDto.class))
                .collect(toList());

        return ResponseEntity.ok(webCategoryDtos);
    }
}
