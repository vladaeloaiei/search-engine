package com.aeloaiei.dissertation.search.engine.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SliceSearchResultDto {
    private SearchResultDto result;
    private int slice;
    private int slices;
}
