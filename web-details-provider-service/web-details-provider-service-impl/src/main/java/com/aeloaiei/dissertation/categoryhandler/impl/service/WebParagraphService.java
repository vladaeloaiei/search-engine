package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebParagraph;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebParagraphService {

    @Autowired
    private WebParagraphRepository webParagraphRepository;

    public List<WebParagraph> getByIds(Collection<String> id) {
        return StreamSupport.stream(webParagraphRepository.findAllById(id).spliterator(), false)
                .collect(toList());
    }
}
