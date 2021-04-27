package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebIntro;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebIntroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebIntroService {

    @Autowired
    private WebIntroRepository webIntroRepository;

    public List<WebIntro> getByLocations(Collection<String> locations) {
        return StreamSupport.stream(webIntroRepository.findAllById(locations).spliterator(), false)
                .collect(toList());
    }
}
