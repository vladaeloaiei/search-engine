package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebCategory;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebCategoryService {
    @Autowired
    private WebCategoryRepository webCategoryRepository;

    public void putAll(List<WebCategory> webCategories) {
        webCategoryRepository.saveAll(webCategories);
    }

    public List<WebCategory> getAll() {
        return webCategoryRepository.findAll();
    }

    public Optional<WebCategory> getByCategory(String category) {
        return webCategoryRepository.findByCategory(category);
    }
}
