package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebTitle;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebTitleService {

    @Autowired
    private WebTitleRepository webTitleRepository;

    public List<WebTitle> getByLocations(Collection<String> locations) {
        return StreamSupport.stream(webTitleRepository.findAllById(locations).spliterator(), false)
                .collect(toList());
    }
}
