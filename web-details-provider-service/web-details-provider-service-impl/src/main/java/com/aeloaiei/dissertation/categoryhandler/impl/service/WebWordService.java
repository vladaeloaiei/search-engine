package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebWord;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebWordService {

    @Autowired
    private WebWordRepository webWordRepository;

    public List<WebWord> getByWords(Collection<String> words) {
        return StreamSupport.stream(webWordRepository.findAllById(words).spliterator(), false)
                .collect(toList());
    }
}
