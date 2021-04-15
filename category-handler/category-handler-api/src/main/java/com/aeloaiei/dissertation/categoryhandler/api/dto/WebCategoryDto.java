package com.aeloaiei.dissertation.categoryhandler.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCategoryDto {
    private String category;
    private Set<String> urls;
}
