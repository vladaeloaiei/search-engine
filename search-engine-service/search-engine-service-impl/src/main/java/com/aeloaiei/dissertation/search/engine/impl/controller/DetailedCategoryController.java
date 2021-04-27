package com.aeloaiei.dissertation.search.engine.impl.controller;

import com.aeloaiei.dissertation.search.engine.api.dto.DetailedCategoryDto;
import com.aeloaiei.dissertation.search.engine.impl.service.DetailedCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RequestMapping("/detailedcategory")
@RestController
public class DetailedCategoryController {

    @Autowired
    private DetailedCategoryService detailedCategoryService;

    @GetMapping
    public ResponseEntity<List<DetailedCategoryDto>> getAll() {
        return ResponseEntity.ok(detailedCategoryService.getAll());
    }
}
