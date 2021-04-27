package com.aeloaiei.dissertation.categoryhandler.impl.service;

import com.aeloaiei.dissertation.categoryhandler.impl.model.WebUrlRank;
import com.aeloaiei.dissertation.categoryhandler.impl.repository.WebUrlRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class WebUrlRankService {

    @Autowired
    private WebUrlRankRepository webUrlRankRepository;

    public List<WebUrlRank> getByLocations(Collection<String> locations) {
        return StreamSupport.stream(webUrlRankRepository.findAllById(locations).spliterator(), false)
                .collect(toList());
    }
}
