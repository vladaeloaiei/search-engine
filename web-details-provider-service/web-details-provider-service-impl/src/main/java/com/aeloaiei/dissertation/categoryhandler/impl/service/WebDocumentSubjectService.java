package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebDocumentSubject;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebDocumentSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebDocumentSubjectService {

    @Autowired
    private WebDocumentSubjectRepository webDocumentSubjectRepository;

    public List<WebDocumentSubject> getByLocations(Collection<String> locations) {
        return StreamSupport.stream(webDocumentSubjectRepository.findAllById(locations).spliterator(), false)
                .collect(toList());
    }
}
